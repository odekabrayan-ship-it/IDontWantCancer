package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandScreenLifecycleInteractionHandoverIntegrityTest {

    private val interactionBridge = mockk<IntelligenceCommandScreenLifecycleInteractionBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandScreenLifecycleInteractionHandoverBoundary(interactionBridge)

    @Test
    fun `handover preserves interaction identity and outcome`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandScreenLifecycleInteractionHandoverRequest(interaction, handler, onNavigate)
        
        boundary.routeToInteraction(request)

        verify { 
            interactionBridge.routeToInteraction(match { 
                it.interaction == interaction && it.handler == handler && it.onNavigate == onNavigate
            }) 
        }
    }
}
