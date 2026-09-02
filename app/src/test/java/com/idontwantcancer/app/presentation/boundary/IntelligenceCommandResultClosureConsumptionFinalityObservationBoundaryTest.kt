package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
class IntelligenceCommandResultClosureConsumptionFinalityObservationBoundaryTest {

    private val closureStream = MutableSharedFlow<IntelligenceCommandClosureResult>()
    private val closureObservationBoundary = mockk<IntelligenceCommandResultClosureObservationBoundary>()
    private val finalityHandoverBridge = mockk<IntelligenceCommandClosureFinalityHandoverBoundary>()
    private val terminalIntegrityHandoverBridge = mockk<IntelligenceCommandFinalityTerminalIntegrityHandoverBoundary>()
    private val projectionHandoverBridge = mockk<IntelligenceCommandTerminalIntegrityProjectionHandoverBoundary>(relaxed = true)
    private val loopCompletionBoundary = mockk<IntelligenceCommandInteractionLoopCompletionBoundary>(relaxed = true)
    private lateinit var boundary: DefaultIntelligenceCommandResultClosureConsumptionFinalityObservationBoundary
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { closureObservationBoundary.closureStream } returns closureStream
        boundary = DefaultIntelligenceCommandResultClosureConsumptionFinalityObservationBoundary(closureObservationBoundary, finalityHandoverBridge, terminalIntegrityHandoverBridge, projectionHandoverBridge, loopCompletionBoundary)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `finality results are observable through the finality stream`() = runTest(testDispatcher) {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL,
            evaluatedAt = Instant.now()
        )
        
        val request = IntelligenceCommandClosureFinalityHandoverRequest(closure)
        every { finalityHandoverBridge.routeToFinality(request) } returns finalityResult
        every { terminalIntegrityHandoverBridge.routeToIntegrity(finalityResult) } returns finalityResult

        val collected = mutableListOf<IntelligenceCommandConsumptionFinalityResult>()
        val job = launch {
            boundary.finalityStream.take(1).toList(collected)
        }

        closureStream.emit(closure)

        assertEquals(1, collected.size)
        assertEquals(finalityResult, collected[0])
        val expectedProjectionHandover = IntelligenceCommandTerminalIntegrityProjectionHandoverRequest(finalityResult)
        verify { projectionHandoverBridge.routeToProjection(expectedProjectionHandover) }
        verify { loopCompletionBoundary.completeLoop(finalityResult) }
        job.cancel()
    }
}
