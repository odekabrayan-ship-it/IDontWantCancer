package com.idontwantcancer.app.presentation.navigation

import androidx.lifecycle.ViewModel
import com.idontwantcancer.app.presentation.boundary.IntelligenceCommandScreenLifecycleInteractionHandoverBoundary
import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary
import com.idontwantcancer.app.presentation.model.IntelligenceCommandScreenLifecycleInteractionHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Helper ViewModel to provide the command dispatcher to the navigation graph.
 */
@HiltViewModel
class NavigationViewModel @Inject constructor(
    val screenInteractionHandover: IntelligenceCommandScreenLifecycleInteractionHandoverBoundary
) : ViewModel() {

    fun dispatch(
        interaction: IntelligenceUiInteraction,
        handler: IntelligenceInteractionBoundary,
        onNavigate: (Any) -> Unit
    ) {
        // Step 199: Route screen interaction through the formalized lifecycle-to-interaction boundary.
        val handoverRequest = IntelligenceCommandScreenLifecycleInteractionHandoverRequest(
            interaction = interaction,
            handler = handler,
            onNavigate = onNavigate
        )
        screenInteractionHandover.routeToInteraction(handoverRequest)
    }
}
