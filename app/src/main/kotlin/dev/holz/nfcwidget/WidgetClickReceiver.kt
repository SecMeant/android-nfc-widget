package dev.holz.nfcwidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter

class WidgetClickReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            NfcToggleWidget.ACTION_TOGGLE -> {
                NfcHelper.toggle(context)
                // Widget UI will refresh when ACTION_ADAPTER_STATE_CHANGED fires.
                // Force an immediate update in case the direct toggle doesn't trigger it
                // (e.g., when opening settings instead of toggling in-place).
                NfcToggleWidget.updateAllWidgets(context)
            }

            NfcAdapter.ACTION_ADAPTER_STATE_CHANGED -> {
                val state = intent.getIntExtra(
                    NfcAdapter.EXTRA_ADAPTER_STATE,
                    NfcAdapter.STATE_OFF
                )
                // Only update when the state has settled (on or off); ignore turning on/off transitions.
                if (state == NfcAdapter.STATE_ON || state == NfcAdapter.STATE_OFF) {
                    NfcToggleWidget.updateAllWidgets(context)
                }
            }
        }
    }
}
