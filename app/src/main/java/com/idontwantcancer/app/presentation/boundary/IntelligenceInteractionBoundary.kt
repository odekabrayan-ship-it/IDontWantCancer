package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for handling UI interactions and translating them 
 * into application commands or navigation.
 */
interface IntelligenceInteractionBoundary {
    /**
     * Handles a user interaction event.
     *
     * @param interaction The UI interaction originating from the renderer.
     */
    fun onInteraction(interaction: IntelligenceUiInteraction)
}
