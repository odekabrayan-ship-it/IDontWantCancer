package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryPostTransitionVerificationResult
import com.idontwantcancer.app.domain.model.IntelligenceReentryTransitionCompletionResult

/**
 * Interface for the intelligence component responsible for formally 
 * closing the lifecycle transition pipeline.
 */
interface IntelligenceReentryTransitionCompletionBoundary {
    /**
     * Finalizes the transition process based on verification results.
     *
     * @param verificationResult The result from Step 99.
     * @return The terminal completion result.
     */
    fun completeTransition(
        verificationResult: IntelligenceReentryPostTransitionVerificationResult
    ): IntelligenceReentryTransitionCompletionResult
}
