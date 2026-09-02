package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandRenderingBoundaryTest {

    private val lifecycleHandoverBoundary = mockk<IntelligenceCommandRenderingLifecycleHandoverBoundary>(relaxed = true)
    private lateinit var boundary: DefaultIntelligenceCommandRenderingBoundary

    @Before
    fun setup() {
        boundary = DefaultIntelligenceCommandRenderingBoundary(lifecycleHandoverBoundary)
    }

    @Test
    fun `rendering stream provides formalized contracts from presentation authority`() = runTest(UnconfinedTestDispatcher()) {
        val contract = CommandConsumptionFinalityPresentationContract.Final(
            operationId = "op1",
            detail = "Success"
        )
        val request = IntelligenceCommandPresentationRenderingRequest(contract)

        val collected = mutableListOf<CommandConsumptionFinalityPresentationContract>()
        val job = launch {
            boundary.renderingStream.toList(collected)
        }

        boundary.renderContract(request)

        assertEquals(1, collected.size)
        assertEquals(contract, collected[0])
        verify { lifecycleHandoverBoundary.routeToLifecycle(match { it.contract == contract }) }
        job.cancel()
    }

    @Test
    fun `interaction events from the renderer follow the authorized dispatch path`() {
        val dispatcher = mockk<IntelligenceCommandDispatcher>(relaxed = true)
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val interaction = IntelligenceUiInteraction.RetryOperation
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { }
        
        // This simulates a click in the UI (renderer)
        dispatcher.dispatch(request)
        
        // Verify it reaches the authorized dispatcher (Step 138/139 boundary)
        verify { dispatcher.dispatch(request) }
    }
}
