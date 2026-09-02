package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that an interpreted interaction (Step 168) 
 * correctly reaches the dispatch authority (Step 169).
 */
interface IntelligenceCommandInteractionDispatchHandoverBoundary {
    /**
     * Routes an interpreted interaction to the dispatch authority.
     *
     * @param interaction The interaction from Step 168.
     * @param handler The authoritative domain handler.
     * @param onNavigate The navigation callback.
     */
    fun routeToDispatch(
        interaction: IntelligenceUiInteraction,
        handler: IntelligenceInteractionBoundary,
        onNavigate: (Any) -> Unit
    )
}
