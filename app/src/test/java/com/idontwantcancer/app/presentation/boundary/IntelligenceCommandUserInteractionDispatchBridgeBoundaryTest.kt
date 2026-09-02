package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandUserInteractionDispatchBridgeBoundaryTest {

    private val dispatchHandover = mockk<IntelligenceCommandInteractionDispatchHandoverBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandUserInteractionDispatchBridgeBoundary(dispatchHandover)

    @Test
    fun `routeToDispatchHandover maps handover request to formal dispatch handover gate`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandUserInteractionDispatchHandoverRequest(interaction, handler, onNavigate)
        
        boundary.routeToDispatchHandover(request)

        verify { 
            dispatchHandover.routeToDispatch(
                interaction = interaction,
                handler = handler,
                onNavigate = onNavigate
            ) 
        }
    }
}
