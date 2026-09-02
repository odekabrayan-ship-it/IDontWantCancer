package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandProjectionPresentationHandoverBoundaryTest {

    private val presentationBridge = mockk<IntelligenceCommandProjectionPresentationBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandProjectionPresentationHandoverBoundary(presentationBridge)

    @Test
    fun `routeToPresentation maps projection outcome to handover request and delegates to presentation authority`() {
        val projection = CommandConsumptionFinalityUiState(
            operationId = "op1",
            isTerminal = true
        )
        val request = IntelligenceCommandProjectionPresentationHandoverRequest(projection)
        
        boundary.routeToPresentation(request)

        verify { presentationBridge.routeToPresentation(match { it.projection == projection }) }
    }
}
