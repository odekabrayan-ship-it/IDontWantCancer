package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that screen interactions (Step 198) 
 * are handled in a lifecycle-aware manner.
 */
interface IntelligenceCommandScreenLifecycleInteractionBoundary {
    /**
     * Handles an interaction from a lifecycle-aware screen.
     *
     * @param interaction The UI interaction.
     * @param handler The authoritative domain handler.
     * @param onNavigate The navigation callback.
     */
    fun handleScreenInteraction(
        interaction: IntelligenceUiInteraction,
        handler: IntelligenceInteractionBoundary,
        onNavigate: (Any) -> Unit
    )
}
