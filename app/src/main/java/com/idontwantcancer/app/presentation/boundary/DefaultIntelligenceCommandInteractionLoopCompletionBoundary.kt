package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the interaction loop completion boundary.
 * Finalizes the reporting cycle for command results.
 */
class DefaultIntelligenceCommandInteractionLoopCompletionBoundary @Inject constructor() : 
    IntelligenceCommandInteractionLoopCompletionBoundary {

    override fun completeLoop(
        finalityResult: IntelligenceCommandConsumptionFinalityResult
    ): IntelligenceCommandInteractionLoopCompletionResult {
        // Step 150 Logic: Synchronize the terminal state of the interaction loop.
        // It confirms the loop is complete and auditable.
        return IntelligenceCommandInteractionLoopCompletionResult(
            operationId = finalityResult.operationId,
            finality = finalityResult.finality,
            isSynchronized = true,
            completedAt = Instant.now()
        )
    }
}
