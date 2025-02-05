package com.example.hiccuplogger

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import android.os.Handler
import android.os.Looper

class HiccupWidget : AppWidgetProvider() {

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
        private const val ACTION_RECORD_HICCUP = "com.example.hiccuplogger.RECORD_HICCUP"

        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, HiccupWidget::class.java).apply {
                action = ACTION_RECORD_HICCUP
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(context.packageName, R.layout.widget_hiccup)
            views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_RECORD_HICCUP) {
            recordHiccup(context)
        }
    }

    private fun recordHiccup(context: Context) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val logFile = File(context.filesDir, "hiccup_log.txt")
        logFile.appendText("h $timestamp\n")
    }
}