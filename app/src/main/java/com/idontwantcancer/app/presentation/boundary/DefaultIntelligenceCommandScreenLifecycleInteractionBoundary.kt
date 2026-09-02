package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandScreenLifecycleInteractionHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import javax.inject.Inject

/**
 * Default implementation of the screen lifecycle interaction boundary.
 * Orchestrates the handover from active screens to the interaction logic.
 */
class DefaultIntelligenceCommandScreenLifecycleInteractionBoundary @Inject constructor(
    private val interactionHandover: IntelligenceCommandScreenLifecycleInteractionHandoverBoundary
) : IntelligenceCommandScreenLifecycleInteractionBoundary {

    override fun handleScreenInteraction(
        interaction: IntelligenceUiInteraction,
        handler: IntelligenceInteractionBoundary,
        onNavigate: (Any) -> Unit
    ) {
        // Step 199 Logic: Handover the screen-level gesture to the interaction authority.
        val handoverRequest = IntelligenceCommandScreenLifecycleInteractionHandoverRequest(
            interaction = interaction,
            handler = handler,
            onNavigate = onNavigate
        )
        interactionHandover.routeToInteraction(handoverRequest)
    }
}
