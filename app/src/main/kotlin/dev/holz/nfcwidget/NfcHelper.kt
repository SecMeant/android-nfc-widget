package dev.holz.nfcwidget

import android.Manifest
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings

object NfcHelper {

    fun isNfcEnabled(context: Context): Boolean {
        return NfcAdapter.getDefaultAdapter(context)?.isEnabled == true
    }

    fun isNfcAvailable(context: Context): Boolean {
        return NfcAdapter.getDefaultAdapter(context) != null
    }

    fun toggle(context: Context) {
        openNfcSettings(context)
    }

    private fun openNfcSettings(context: Context) {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
