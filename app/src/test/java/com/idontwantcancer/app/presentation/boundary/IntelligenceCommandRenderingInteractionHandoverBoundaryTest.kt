package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandRenderingInteractionHandoverBoundaryTest {

    private val interactionBridge = mockk<IntelligenceCommandRenderingInteractionBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandRenderingInteractionHandoverBoundary(interactionBridge)

    @Test
    fun `routeToInteraction maps user action to handover request and delegates to bridge`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        
        boundary.routeToInteraction(interaction, handler, onNavigate)

        verify { 
            interactionBridge.routeToInteraction(match { 
                it.interaction == interaction && it.handler == handler 
            }) 
        }
    }
}
