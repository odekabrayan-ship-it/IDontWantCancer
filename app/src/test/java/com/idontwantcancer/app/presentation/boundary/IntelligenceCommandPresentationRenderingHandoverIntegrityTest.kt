package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandPresentationRenderingHandoverIntegrityTest {

    private val renderingBridge = mockk<IntelligenceCommandPresentationRenderingBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandPresentationRenderingHandoverBoundary(renderingBridge)

    @Test
    fun `handover preserves presentation identity and outcome`() {
        val contract = CommandConsumptionFinalityPresentationContract.Final(
            operationId = "op197",
            detail = "Pixels"
        )
        val request = IntelligenceCommandPresentationRenderingHandoverRequest(contract)
        
        boundary.routeToRendering(request)

        verify { 
            renderingBridge.routeToRendering(match { 
                it.contract == contract && it.contract.operationId == "op197" 
            }) 
        }
    }
}
