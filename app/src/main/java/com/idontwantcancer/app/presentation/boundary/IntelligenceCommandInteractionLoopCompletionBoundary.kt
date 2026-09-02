package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandInteractionLoopCompletionResult

/**
 * Authoritative boundary for formally completing the interaction loop 
 * for application command results.
 */
interface IntelligenceCommandInteractionLoopCompletionBoundary {
    /**
     * Completes the interaction loop based on the finality establishment.
     *
     * @param finalityResult The terminal finality record (Step 132 product).
     * @return The structured interaction loop completion result.
     */
    fun completeLoop(
        finalityResult: IntelligenceCommandConsumptionFinalityResult
    ): IntelligenceCommandInteractionLoopCompletionResult
}
