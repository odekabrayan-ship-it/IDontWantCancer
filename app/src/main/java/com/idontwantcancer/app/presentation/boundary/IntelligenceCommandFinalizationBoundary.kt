package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary where an already-established command outcome becomes 
 * the final command result consumed by the command architecture.
 */
interface IntelligenceCommandFinalizationBoundary {
    /**
     * Finalizes the outcome of a command interaction.
     *
     * @param interaction The UI interaction being finalized.
     * @param result The established result from the authoritative result boundary.
     * @return The authoritative terminal result.
     */
    fun finalizeResult(
        interaction: IntelligenceUiInteraction,
        result: IntelligenceApplicationCommandResult
    ): IntelligenceApplicationCommandResult
}
