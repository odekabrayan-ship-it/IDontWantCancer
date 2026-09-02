package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandProjectionPresentationBridgeBoundaryTest {

    private val presentationAuthority = mockk<CommandConsumptionFinalityPresentationContractBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandProjectionPresentationBridgeBoundary(presentationAuthority)

    @Test
    fun `routeToPresentation maps projection handover request and delegates to authoritative gate`() {
        val projection = CommandConsumptionFinalityUiState(
            operationId = "op1",
            isTerminal = true
        )
        val request = IntelligenceCommandProjectionPresentationHandoverRequest(projection)
        
        boundary.routeToPresentation(request)

        verify { presentationAuthority.presentProjection(match { it.projection == projection }) }
    }
}
