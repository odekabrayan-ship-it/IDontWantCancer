package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionOutcome
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that execution outcomes correctly enter 
 * the existing result authority.
 */
interface IntelligenceCommandExecutionOutcomeBoundary {
    /**
     * Reports an execution outcome to the result authority.
     *
     * @param interaction The interaction that was executed.
     * @param outcome The raw outcome of the execution.
     * @return The authoritative command result established.
     */
    fun reportOutcome(
        interaction: IntelligenceUiInteraction,
        outcome: IntelligenceCommandExecutionOutcome
    ): IntelligenceApplicationCommandResult
}
