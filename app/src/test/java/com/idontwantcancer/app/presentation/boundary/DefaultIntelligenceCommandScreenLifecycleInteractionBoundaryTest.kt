package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class DefaultIntelligenceCommandScreenLifecycleInteractionBoundaryTest {

    private val interactionHandover = mockk<IntelligenceCommandScreenLifecycleInteractionHandoverBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandScreenLifecycleInteractionBoundary(interactionHandover)

    @Test
    fun `handleScreenInteraction routes handover request to interaction authority`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        
        boundary.handleScreenInteraction(interaction, handler, onNavigate)

        verify { 
            interactionHandover.routeToInteraction(match { 
                it.interaction == interaction && it.handler == handler && it.onNavigate == onNavigate
            }) 
        }
    }
}
