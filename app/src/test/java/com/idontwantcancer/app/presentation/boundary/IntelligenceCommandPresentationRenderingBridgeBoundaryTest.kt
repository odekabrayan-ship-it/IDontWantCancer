package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandPresentationRenderingBridgeBoundaryTest {

    private val renderingAuthority = mockk<IntelligenceCommandRenderingBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandPresentationRenderingBridgeBoundary(renderingAuthority)

    @Test
    fun `routeToRendering maps handover request to internal rendering request and delegates`() {
        val contract = CommandConsumptionFinalityPresentationContract.Final(
            operationId = "op1",
            detail = "Pixels"
        )
        val request = IntelligenceCommandPresentationRenderingHandoverRequest(contract)
        
        boundary.routeToRendering(request)

        verify { renderingAuthority.renderContract(match { it.contract == contract }) }
    }
}
