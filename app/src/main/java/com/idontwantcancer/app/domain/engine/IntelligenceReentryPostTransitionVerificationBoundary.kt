package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryPostTransitionVerificationResult
import com.idontwantcancer.app.domain.model.ReentryTransitionResult

/**
 * Interface for the intelligence component responsible for verifying 
 * that authoritative state is consistent after a lifecycle transition.
 */
interface IntelligenceReentryPostTransitionVerificationBoundary {
    /**
     * Verifies the result of a lifecycle transition.
     *
     * @param transitionResult The result of the transition execution (Step 98).
     * @return The structured verification result.
     */
    suspend fun verifyTransition(
        transitionResult: ReentryTransitionResult
    ): IntelligenceReentryPostTransitionVerificationResult
}
