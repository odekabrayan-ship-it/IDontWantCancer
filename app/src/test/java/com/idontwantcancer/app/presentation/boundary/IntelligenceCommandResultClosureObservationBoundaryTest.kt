package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandResultClosureObservationBoundaryTest {

    private val resultStream = MutableSharedFlow<IntelligenceApplicationCommandResult>()
    private val consumptionBoundary = mockk<IntelligenceCommandResultConsumptionBoundary>()
    private val closureBoundary = mockk<IntelligenceCommandResultClosureBoundary>()
    private lateinit var boundary: DefaultIntelligenceCommandResultClosureObservationBoundary
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { consumptionBoundary.resultStream } returns resultStream
        boundary = DefaultIntelligenceCommandResultClosureObservationBoundary(consumptionBoundary, closureBoundary)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `closure results are observable through the closure stream`() = runTest(testDispatcher) {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val closureResult = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )
        
        coEvery { closureBoundary.evaluateClosure(result) } returns closureResult

        val collected = mutableListOf<IntelligenceCommandClosureResult>()
        val job = launch {
            boundary.closureStream.take(1).toList(collected)
        }

        resultStream.emit(result)

        assertEquals(1, collected.size)
        assertEquals(closureResult, collected[0])
        job.cancel()
    }
}
