package dev.holz.nfcwidget

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.provider.Settings

object NfcHelper {

    fun isNfcEnabled(context: Context): Boolean {
        return NfcAdapter.getDefaultAdapter(context)?.isEnabled == true
    }

    fun isNfcAvailable(context: Context): Boolean {
        return NfcAdapter.getDefaultAdapter(context) != null
    }

    /**
     * Toggles NFC directly if WRITE_SECURE_SETTINGS is granted (via ADB),
     * otherwise opens the NFC settings page for the user to toggle manually.
     *
     * Grant the permission once with:
     *   adb shell pm grant dev.holz.nfcwidget android.permission.WRITE_SECURE_SETTINGS
     */
    fun toggle(context: Context) {
        if (hasWriteSecureSettings(context)) {
            toggleDirectly(context)
        } else {
            openNfcSettings(context)
        }
    }

    private fun hasWriteSecureSettings(context: Context): Boolean {
        return context.checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun toggleDirectly(context: Context) {
        val currentlyEnabled = isNfcEnabled(context)
        Settings.Global.putInt(
            context.contentResolver,
            "nfc_on",
            if (currentlyEnabled) 0 else 1
        )
    }

    private fun openNfcSettings(context: Context) {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
