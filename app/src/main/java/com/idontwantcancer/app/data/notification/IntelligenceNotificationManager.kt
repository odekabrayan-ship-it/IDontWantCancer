package com.idontwantcancer.app.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.idontwantcancer.app.MainActivity
import com.idontwantcancer.app.R
import com.idontwantcancer.app.domain.model.BriefingStatus
import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.model.SignalImportance
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntelligenceNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val CHANNEL_ID_BRIEFING = "intelligence_briefing"
        private const val CHANNEL_ID_URGENT = "urgent_safety_alerts"
        private const val NOTIFICATION_ID_BRIEFING = 1001
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val briefingChannel = NotificationChannel(
                CHANNEL_ID_BRIEFING,
                context.getString(R.string.channel_briefing_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.channel_briefing_desc)
            }

            val urgentChannel = NotificationChannel(
                CHANNEL_ID_URGENT,
                context.getString(R.string.channel_urgent_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.channel_urgent_desc)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(briefingChannel)
            manager.createNotificationChannel(urgentChannel)
        }
    }

    fun showBriefingNotification(briefing: IntelligenceBriefing) {
        if (briefing.status == BriefingStatus.NO_MAJOR_CHANGES) return

        val title = when (briefing.status) {
            BriefingStatus.ATTENTION_REQUIRED -> context.getString(R.string.notif_urgent_title)
            else -> context.getString(R.string.notif_ready_title)
        }

        val content = if (briefing.items.isNotEmpty()) {
            briefing.items.first().explanation?.whatChanged ?: briefing.items.first().inclusionReason
        } else {
            context.getString(R.string.notif_generic_content)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(
            context,
            if (briefing.status == BriefingStatus.ATTENTION_REQUIRED) CHANNEL_ID_URGENT else CHANNEL_ID_BRIEFING
        )
            .setSmallIcon(R.drawable.ic_notification) // We'll need to create this or use launcher
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(if (briefing.status == BriefingStatus.ATTENTION_REQUIRED) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(NOTIFICATION_ID_BRIEFING, builder.build())
            } catch (e: SecurityException) {
                // Permission not granted
            }
        }
    }

    fun showSignalDirectiveNotification(signal: Signal) {
        val title = context.getString(R.string.notif_directive_prefix, signal.theCommand ?: signal.title)
        
        val bigText = buildString {
            append(signal.summary)
            if (signal.theExecution.isNotEmpty()) {
                append("\n\n")
                append(context.getString(R.string.detail_how_to_title))
                append(":\n")
                signal.theExecution.take(3).forEachIndexed { index, step ->
                    append("${index + 1}. $step\n")
                }
            }
            append("\n")
            append(context.getString(R.string.notif_view_manual))
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("signalId", signal.id)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, signal.id.hashCode(), intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_URGENT)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(signal.summary)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(signal.id.hashCode(), builder.build())
            } catch (e: SecurityException) {
                // Permission not granted
            }
        }
    }
}
