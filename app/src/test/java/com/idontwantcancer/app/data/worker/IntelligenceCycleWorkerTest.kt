package com.idontwantcancer.app.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.idontwantcancer.app.domain.engine.IntelligenceCycleCoordinator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCycleWorkerTest {

    private val coordinator = mockk<IntelligenceCycleCoordinator>()
    private val context = mockk<Context>(relaxed = true)
    private val workerParams = mockk<WorkerParameters>(relaxed = true)

    @Test
    fun `worker should invoke coordinator and return success`() = runTest {
        val worker = IntelligenceCycleWorker(context, workerParams, coordinator)
        coEvery { coordinator.runCycle() } returns mockk()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        coVerify { coordinator.runCycle() }
    }

    @Test
    fun `worker should return retry on transient failure`() = runTest {
        val worker = IntelligenceCycleWorker(context, workerParams, coordinator)
        coEvery { coordinator.runCycle() } throws RuntimeException("Transient error")
        // Note: runAttemptCount is mocked to 0 by default for relaxed mockk

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
    }
}
