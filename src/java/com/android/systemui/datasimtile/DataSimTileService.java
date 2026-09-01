package com.android.systemui.datasimtile;

import android.content.ContentResolver;
import android.graphics.drawable.Icon;
import android.os.IBinder;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Method;

public class DataSimTileService extends TileService {

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        updateTileState();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTileState();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        try {
            // Access internal telephony ISub service
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            Method getService = serviceManager.getMethod("getService", String.class);
            IBinder binder = (IBinder) getService.invoke(null, "isub");

            Class<?> stubClass = Class.forName("com.android.internal.telephony.ISub$Stub");
            Method asInterface = stubClass.getMethod("asInterface", IBinder.class);
            Object iSub = asInterface.invoke(null, binder);

            if (iSub == null) return;

            Method getDefaultDataSubId = iSub.getClass().getMethod("getDefaultDataSubId");
            int currentSubId = (int) getDefaultDataSubId.invoke(iSub);

            // Toggle between SIM 1 (subId 2) and SIM 2 (subId 1)
            int targetSubId;
            String label;
            int iconRes;

            if (currentSubId == 1) {
                targetSubId = 2;
                label = "Data: SIM 1";
                iconRes = R.drawable.ic_sim_1;
            } else {
                targetSubId = 1;
                label = "Data: SIM 2";
                iconRes = R.drawable.ic_sim_2;
            }

            // 1. Update Tile UI Immediately
            Tile tile = getQsTile();
            if (tile != null) {
                tile.setState(Tile.STATE_ACTIVE);
                tile.setLabel(label);
                tile.setIcon(Icon.createWithResource(this, iconRes));
                tile.updateTile();
            }

            // 2. Set Default Data Subscription via ISub
            Method setDefaultDataSubId = iSub.getClass().getMethod("setDefaultDataSubId", int.class);
            setDefaultDataSubId.invoke(iSub, targetSubId);

            // 3. Update TelephonyManager data states
            TelephonyManager tm = getSystemService(TelephonyManager.class);
            if (tm != null) {
                try {
                    TelephonyManager subTm = tm.createForSubscriptionId(targetSubId);
                    subTm.setDataEnabledForReason(TelephonyManager.DATA_ENABLED_REASON_USER, true);
                    tm.setDataEnabled(targetSubId, true);
                    tm.setDataEnabled(currentSubId, false);
                } catch (Throwable ignored) {}
            }

            // 4. Update Settings.Global
            ContentResolver cr = getContentResolver();
            Settings.Global.putInt(cr, "multi_sim_data_call", targetSubId);
            Settings.Global.putInt(cr, "user_preferred_data_sub", targetSubId);

            // 5. User Feedback Toast
            Toast.makeText(this, "Switched to " + label, Toast.LENGTH_SHORT).show();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void updateTileState() {
        try {
            Tile tile = getQsTile();
            if (tile == null) return;

            tile.setState(Tile.STATE_ACTIVE);

            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            Method getService = serviceManager.getMethod("getService", String.class);
            IBinder binder = (IBinder) getService.invoke(null, "isub");

            Class<?> stubClass = Class.forName("com.android.internal.telephony.ISub$Stub");
            Method asInterface = stubClass.getMethod("asInterface", IBinder.class);
            Object iSub = asInterface.invoke(null, binder);

            if (iSub == null) return;

            Method getDefaultDataSubId = iSub.getClass().getMethod("getDefaultDataSubId");
            int currentSubId = (int) getDefaultDataSubId.invoke(iSub);

            if (currentSubId == 1) {
                tile.setLabel("Data: SIM 2");
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_sim_2));
            } else {
                tile.setLabel("Data: SIM 1");
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_sim_1));
            }

            tile.updateTile();

        } catch (Throwable ignored) {}
    }
}
