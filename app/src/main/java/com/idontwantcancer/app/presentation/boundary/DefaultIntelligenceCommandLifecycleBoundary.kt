package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandLifecycleStage
import com.idontwantcancer.app.presentation.model.IntelligenceCommandLifecycleStatus
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command lifecycle boundary.
 * Produces deterministic status records for command progression.
 */
class DefaultIntelligenceCommandLifecycleBoundary @Inject constructor() : 
    IntelligenceCommandLifecycleBoundary {

    override fun transitionTo(
        interaction: IntelligenceUiInteraction,
        stage: CommandLifecycleStage
    ): IntelligenceCommandLifecycleStatus {
        // Deterministic transition record.
        // It does not persist the status, as per Step 113 rules.
        return IntelligenceCommandLifecycleStatus(
            interaction = interaction,
            stage = stage,
            timestamp = Instant.now()
        )
    }
}
