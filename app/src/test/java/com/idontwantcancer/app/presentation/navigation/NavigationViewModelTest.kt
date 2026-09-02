package com.idontwantcancer.app.presentation.navigation

import com.idontwantcancer.app.presentation.boundary.IntelligenceCommandScreenLifecycleInteractionHandoverBoundary
import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import io.mockk.*
import org.junit.Test

class NavigationViewModelTest {

    private val screenInteractionHandover = mockk<IntelligenceCommandScreenLifecycleInteractionHandoverBoundary>(relaxed = true)
    private val viewModel = NavigationViewModel(screenInteractionHandover)

    @Test
    fun `dispatch routes screen interaction through handover boundary`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        
        viewModel.dispatch(interaction, handler, onNavigate)

        verify { 
            screenInteractionHandover.routeToInteraction(match { 
                it.interaction == interaction && it.handler == handler && it.onNavigate == onNavigate
            }) 
        }
    }
}
