package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class CommandConsumptionFinalityProjectionBoundaryTest {

    private val presentationBridge = mockk<IntelligenceCommandProjectionPresentationHandoverBoundary>(relaxed = true)
    private lateinit var boundary: DefaultCommandConsumptionFinalityProjectionBoundary
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        boundary = DefaultCommandConsumptionFinalityProjectionBoundary(presentationBridge)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `terminal integrity result is correctly projected to UI state and routed to presentation`() = runTest(testDispatcher) {
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL,
            reason = "Success",
            evaluatedAt = Instant.now()
        )
        val request = IntelligenceCommandTerminalIntegrityProjectionHandoverRequest(finalityResult)

        val collected = mutableListOf<CommandConsumptionFinalityUiState>()
        val job = launch {
            boundary.finalityProjectionStream.take(1).toList(collected)
        }

        boundary.projectTerminality(request)

        assertEquals(1, collected.size)
        assertEquals("op1", collected[0].operationId)
        assertEquals(true, collected[0].isTerminal)
        assertEquals("Success", collected[0].detail)
        verify { presentationBridge.routeToPresentation(match { it.projection.operationId == "op1" }) }
        job.cancel()
    }
}
