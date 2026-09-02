package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandPresentationRenderingHandoverBoundaryTest {

    private val renderingBridge = mockk<IntelligenceCommandPresentationRenderingBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandPresentationRenderingHandoverBoundary(renderingBridge)

    @Test
    fun `routeToRendering maps presentation contract to handover request and delegates to bridge`() {
        val contract = CommandConsumptionFinalityPresentationContract.Final(
            operationId = "op1",
            detail = "Pixels"
        )
        val request = IntelligenceCommandPresentationRenderingHandoverRequest(contract)
        
        boundary.routeToRendering(request)

        verify { renderingBridge.routeToRendering(match { it.contract == contract }) }
    }
}
