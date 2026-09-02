package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandInteractionDispatchHandoverBoundaryTest {

    private val dispatchBridge = mockk<IntelligenceCommandInteractionDispatchBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandInteractionDispatchHandoverBoundary(dispatchBridge)

    @Test
    fun `routeToDispatch maps interaction to handover request and delegates to bridge`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        
        boundary.routeToDispatch(interaction, handler, onNavigate)

        verify { 
            dispatchBridge.routeToDispatch(match { 
                it.interaction == interaction && it.handler == handler 
            }) 
        }
    }
}
