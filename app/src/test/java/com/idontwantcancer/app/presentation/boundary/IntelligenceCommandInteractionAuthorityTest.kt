package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandInteractionAuthorityTest {

    private val dispatchHandoverBridge = mockk<IntelligenceCommandUserInteractionDispatchHandoverBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandInteractionAuthority(dispatchHandoverBridge)

    @Test
    fun `handleInteraction delegates to dispatch bridge`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandRenderingInteractionRequest(interaction, handler, onNavigate)
        
        boundary.handleInteraction(request)

        verify { 
            dispatchHandoverBridge.routeToDispatchHandover(match { 
                it.interaction == interaction && it.handler == handler && it.onNavigate == onNavigate
            }) 
        }
    }
}
