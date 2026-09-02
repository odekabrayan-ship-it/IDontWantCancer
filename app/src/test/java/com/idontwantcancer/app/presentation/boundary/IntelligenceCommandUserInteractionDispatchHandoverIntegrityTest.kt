package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandUserInteractionDispatchHandoverIntegrityTest {

    private val userInteractionBridge = mockk<IntelligenceCommandUserInteractionDispatchBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandUserInteractionDispatchHandoverBoundary(userInteractionBridge)

    @Test
    fun `handover preserves interaction identity and outcome`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandUserInteractionDispatchHandoverRequest(interaction, handler, onNavigate)
        
        boundary.routeToDispatchHandover(request)

        verify { 
            userInteractionBridge.routeToDispatchHandover(match { 
                it.interaction == interaction && it.handler == handler && it.onNavigate == onNavigate
            }) 
        }
    }
}
