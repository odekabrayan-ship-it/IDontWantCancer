package com.idontwantcancer.app.data.worker

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Responsible for scheduling the autonomous intelligence cycle.
 */
@Singleton
class IntelligenceCycleScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val WORK_NAME = "com.idontwantcancer.app.INTELLIGENCE_CYCLE"
        private const val REPEAT_INTERVAL_HOURS = 12L
    }

    /**
     * Schedules the periodic intelligence cycle if it's not already scheduled.
     */
    fun schedule() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<IntelligenceCycleWorker>(
            REPEAT_INTERVAL_HOURS, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Idempotent: don't replace if already exists
            workRequest
        )
    }
}
