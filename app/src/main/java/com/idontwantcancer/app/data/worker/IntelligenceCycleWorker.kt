package com.idontwantcancer.app.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.idontwantcancer.app.domain.engine.IntelligenceCycleCoordinator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * WorkManager worker responsible for triggering the autonomous intelligence cycle.
 */
@HiltWorker
class IntelligenceCycleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val coordinator: IntelligenceCycleCoordinator
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val result = coordinator.runCycle()
            
            // This is an intelligence system. Even if some sources failed, 
            // the cycle might be considered successful if the overall process completed.
            // Fatal failures should be caught by the catch block.
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
