# 📶 Data SIM Quick Settings Tile

[![Android 10+](https://img.shields.io/badge/Android-10%2B%20%7C%2014%20%7C%2015%20%7C%2016-00E5FF?style=for-the-badge&logo=android)](https://github.com/amintum)
[![License: CC BY-NC-SA 4.0](https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0%20%2B%20Attribution-blue?style=for-the-badge)](LICENSE)

A lightning-fast, 1-tap **Data SIM Switcher Quick Settings Tile** for Dual-SIM Android devices. Switch mobile data seamlessly between **SIM 1** and **SIM 2** directly from your status bar without opening Settings!

---

## ⚡ Features
* 🔄 **Instant 1-Tap Switching**: Toggle mobile data between SIM 1 & SIM 2 in under 100ms.
* 🏷️ **Dynamic Tile Label & Icon**: Shows active SIM number directly on the tile (`Data: SIM 1` / `Data: SIM 2`).
* 🔒 **Zero Background Battery Drain**: Runs strictly on-demand via native AOSP `TileService`.
* 📱 **Universal Support**: Works seamlessly on Android 10, 11, 12, 13, 14, 15, and 16 (AOSP, LineageOS, Pixel Experience, GSI).

---

## 📥 Installation & Usage

### 🔹 Option 1: Standalone Sideload (Without ROM Modification)
You can install this APK on any standard Dual-SIM Android device via ADB:

1. **Download & Install the APK**:
   ```bash
   adb install releases/DataSimTile.apk
   ```
2. **Grant Necessary Telephony & System Permissions via ADB**:
   ```bash
   adb shell pm grant com.android.systemui.datasimtile android.permission.READ_PHONE_STATE
   adb shell pm grant com.android.systemui.datasimtile android.permission.WRITE_SECURE_SETTINGS
   ```
3. **Add the Tile**:
   * Pull down the notification shade and tap the **Edit (Pencil) icon**.
   * Drag **Data SIM** into your active Quick Settings tiles.

---

### 🔹 Option 2: Full ROM / GSI Integration (For ROM Builders)
To bake `DataSimTile` directly into your Android ROM/GSI image:

1. **Place APK into Privileged Partition**:
   ```text
   /system/priv-app/DataSimTile/DataSimTile.apk
   # OR
   /system/product/priv-app/DataSimTile/DataSimTile.apk
   ```
2. **Place Permissions Whitelist**:
   Copy `permissions/privapp-permissions-datasimtile.xml` to:
   ```text
   /system/etc/permissions/privapp-permissions-datasimtile.xml
   ```
3. **(Optional) Set as Default QS Tile**:
   Add `custom(com.android.systemui.datasimtile/.DataSimTileService)` to `quick_settings_tiles_default` in `frameworks/base/packages/SystemUI/res/values/config.xml`.

---

## 🛠️ Building from Source
This repository includes ready-to-compile Java sources and resources. Build using Android SDK `aapt2`, `javac`, `d8`, and `apksigner`:
```bash
# Compile and package using the included build toolchain
python build_apk.py
```

---

## 📜 License & Mandatory Credit
This project is licensed under **CC BY-NC-SA 4.0 with Mandatory Attribution**.
* **Credit**: If you use, fork, or integrate this app/code into any ROM or distribution, you **MUST** credit **Amintum / BestGSI** prominently.
* Commercial use requires explicit permission.

Developed with ❤️ by **[Amintum](https://github.com/amintum)** for **BestGSI**.
