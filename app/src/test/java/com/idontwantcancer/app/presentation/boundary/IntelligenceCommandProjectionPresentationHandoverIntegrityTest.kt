package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandProjectionPresentationHandoverIntegrityTest {

    private val presentationBridge = mockk<IntelligenceCommandProjectionPresentationBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandProjectionPresentationHandoverBoundary(presentationBridge)

    @Test
    fun `handover preserves projection identity and outcome`() {
        val projection = CommandConsumptionFinalityUiState(
            operationId = "op196",
            isTerminal = true
        )
        val request = IntelligenceCommandProjectionPresentationHandoverRequest(projection)
        
        boundary.routeToPresentation(request)

        verify { 
            presentationBridge.routeToPresentation(match { 
                it.projection == projection && it.projection.operationId == "op196" 
            }) 
        }
    }
}
