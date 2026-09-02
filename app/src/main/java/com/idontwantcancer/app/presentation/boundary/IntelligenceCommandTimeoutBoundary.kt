package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Interface for the component responsible for defining and enforcing 
 * command-level timeout policies.
 */
interface IntelligenceCommandTimeoutBoundary {
    /**
     * Determines whether a timeout should be applied to an interaction.
     *
     * @param interaction The UI interaction to evaluate.
     * @return The timeout duration in milliseconds, or null if no policy exists.
     */
    fun getTimeoutPolicy(interaction: IntelligenceUiInteraction): Long?
}
