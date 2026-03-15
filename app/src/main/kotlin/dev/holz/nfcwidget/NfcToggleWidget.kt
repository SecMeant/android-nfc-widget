package dev.holz.nfcwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class NfcToggleWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        const val ACTION_TOGGLE = "dev.holz.nfcwidget.TOGGLE_NFC"

        fun updateAllWidgets(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                ComponentName(context, NfcToggleWidget::class.java)
            )
            for (id in ids) {
                updateWidget(context, manager, id)
            }
        }

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val nfcEnabled = NfcHelper.isNfcEnabled(context)
            val nfcAvailable = NfcHelper.isNfcAvailable(context)

            val views = RemoteViews(context.packageName, R.layout.widget_nfc)

            if (!nfcAvailable) {
                views.setImageViewResource(R.id.nfc_icon, R.drawable.ic_nfc_off)
                views.setTextViewText(R.id.nfc_label, context.getString(R.string.nfc_unavailable))
                views.setInt(R.id.widget_root, "setBackgroundResource", R.drawable.widget_background_off)
            } else if (nfcEnabled) {
                views.setImageViewResource(R.id.nfc_icon, R.drawable.ic_nfc_on)
                views.setTextViewText(R.id.nfc_label, context.getString(R.string.nfc_on))
                views.setInt(R.id.widget_root, "setBackgroundResource", R.drawable.widget_background_on)
            } else {
                views.setImageViewResource(R.id.nfc_icon, R.drawable.ic_nfc_off)
                views.setTextViewText(R.id.nfc_label, context.getString(R.string.nfc_off))
                views.setInt(R.id.widget_root, "setBackgroundResource", R.drawable.widget_background_off)
            }

            val toggleIntent = Intent(context, WidgetClickReceiver::class.java).apply {
                action = ACTION_TOGGLE
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toggleIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
