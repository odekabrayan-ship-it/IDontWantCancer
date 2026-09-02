package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandRenderingLifecycleHandoverIntegrityTest {

    private val lifecycleBridge = mockk<IntelligenceCommandRenderingLifecycleBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandRenderingLifecycleHandoverBoundary(lifecycleBridge)

    @Test
    fun `handover preserves rendering contract identity and outcome`() {
        val contract = CommandConsumptionFinalityPresentationContract.Final(
            operationId = "op198",
            detail = "Life"
        )
        val request = IntelligenceCommandRenderingLifecycleHandoverRequest(contract)
        
        boundary.routeToLifecycle(request)

        verify { 
            lifecycleBridge.routeToLifecycle(match { 
                it.contract == contract && it.contract.operationId == "op198" 
            }) 
        }
    }
}
