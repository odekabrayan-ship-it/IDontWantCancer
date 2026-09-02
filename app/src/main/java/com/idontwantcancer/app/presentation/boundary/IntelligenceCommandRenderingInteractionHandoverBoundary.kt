package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that a rendered user action (Step 167) 
 * correctly reaches the interaction authority (Step 168).
 */
interface IntelligenceCommandRenderingInteractionHandoverBoundary {
    /**
     * Routes a rendered user action to the interaction authority.
     *
     * @param interaction The explicit user interaction.
     * @param handler The authoritative domain handler.
     * @param onNavigate The navigation callback.
     */
    fun routeToInteraction(
        interaction: IntelligenceUiInteraction,
        handler: IntelligenceInteractionBoundary,
        onNavigate: (Any) -> Unit
    )
}
