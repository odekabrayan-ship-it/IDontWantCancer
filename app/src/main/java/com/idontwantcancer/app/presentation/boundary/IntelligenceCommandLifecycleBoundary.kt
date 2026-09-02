package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandLifecycleStage
import com.idontwantcancer.app.presentation.model.IntelligenceCommandLifecycleStatus
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for tracking and formalizing the operational 
 * lifecycle of application commands.
 */
interface IntelligenceCommandLifecycleBoundary {
    /**
     * Formalizes a stage transition for an application command.
     *
     * @param interaction The interaction being tracked.
     * @param stage The new lifecycle stage.
     * @return The updated lifecycle status.
     */
    fun transitionTo(
        interaction: IntelligenceUiInteraction,
        stage: CommandLifecycleStage
    ): IntelligenceCommandLifecycleStatus
}
