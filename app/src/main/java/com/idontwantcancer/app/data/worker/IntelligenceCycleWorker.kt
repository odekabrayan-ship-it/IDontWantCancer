package com.idontwantcancer.app.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.idontwantcancer.app.data.notification.IntelligenceNotificationManager
import com.idontwantcancer.app.domain.engine.IntelligenceCycleCoordinator
import com.idontwantcancer.app.domain.model.SignalImportance
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * WorkManager worker responsible for triggering the autonomous intelligence cycle.
 */
@HiltWorker
class IntelligenceCycleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val coordinator: IntelligenceCycleCoordinator,
    private val notificationManager: IntelligenceNotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val result = coordinator.runCycle()
            
            // Step 222 Overhaul: Show specific directive notifications for high-priority signals
            result.briefing?.let { briefing ->
                val criticalSignals = briefing.items
                    .mapNotNull { briefing.signals[it.intelligenceId] }
                    .filter { it.importance == SignalImportance.CRITICAL || it.importance == SignalImportance.HIGH }

                if (criticalSignals.isNotEmpty()) {
                    criticalSignals.forEach { signal ->
                        notificationManager.showSignalDirectiveNotification(signal)
                    }
                } else {
                    notificationManager.showBriefingNotification(briefing)
                }
            }

            Result.success()
        } catch (e: Exception) {
            // For transient failures, we can retry with backoff.
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
