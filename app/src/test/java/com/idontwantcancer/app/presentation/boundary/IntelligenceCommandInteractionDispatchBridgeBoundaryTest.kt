package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandInteractionDispatchBridgeBoundaryTest {

    private val dispatchAuthority = mockk<IntelligenceCommandInteractionDispatchBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandInteractionDispatchBridgeBoundary(dispatchAuthority)

    @Test
    fun `routeToDispatch maps handover request to internal dispatch request and delegates`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandInteractionDispatchHandoverRequest(interaction, handler, onNavigate)
        
        boundary.routeToDispatch(request)

        verify { 
            dispatchAuthority.dispatchInteraction(match { 
                it.interaction == interaction && it.handler == handler 
            }) 
        }
    }
}
