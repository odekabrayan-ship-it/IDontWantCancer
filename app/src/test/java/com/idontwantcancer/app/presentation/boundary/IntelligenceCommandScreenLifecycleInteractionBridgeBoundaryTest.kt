package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandScreenLifecycleInteractionBridgeBoundaryTest {

    private val interactionAuthority = mockk<IntelligenceCommandInteractionAuthority>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandScreenLifecycleInteractionBridgeBoundary(interactionAuthority)

    @Test
    fun `routeToInteraction maps handover request to internal interaction request and delegates`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandScreenLifecycleInteractionHandoverRequest(interaction, handler, onNavigate)
        
        boundary.routeToInteraction(request)

        verify { 
            interactionAuthority.handleInteraction(match { 
                it.interaction == interaction && it.handler == handler && it.onNavigate == onNavigate
            }) 
        }
    }
}
