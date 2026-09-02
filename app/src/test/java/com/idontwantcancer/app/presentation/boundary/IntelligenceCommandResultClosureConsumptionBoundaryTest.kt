package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
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
class IntelligenceCommandResultClosureConsumptionBoundaryTest {

    private val closureStream = MutableSharedFlow<IntelligenceCommandClosureResult>()
    private val observationBoundary = mockk<IntelligenceCommandResultClosureObservationBoundary>()
    private lateinit var boundary: DefaultIntelligenceCommandResultClosureConsumptionBoundary
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { observationBoundary.closureStream } returns closureStream
        boundary = DefaultIntelligenceCommandResultClosureConsumptionBoundary(observationBoundary)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `closure results are consumable through the stream`() = runTest(testDispatcher) {
        val closureResult = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )

        val collected = mutableListOf<IntelligenceCommandClosureResult>()
        val job = launch {
            boundary.closureStream.take(1).toList(collected)
        }

        closureStream.emit(closureResult)

        assertEquals(1, collected.size)
        assertEquals(closureResult, collected[0])
        job.cancel()
    }

    @Test
    fun `consumeClosure retrieves specific closure fact from stream`() = runTest(testDispatcher) {
        val closureResult = IntelligenceCommandClosureResult(
            operationId = "op2",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )

        val job = launch {
            val result = boundary.consumeClosure("op2")
            assertEquals(closureResult, result)
        }

        closureStream.emit(closureResult)
        job.join()
    }
}
