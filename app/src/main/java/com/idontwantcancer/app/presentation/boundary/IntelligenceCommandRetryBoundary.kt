package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRetryResult
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Interface for the component responsible for defining and enforcing 
 * command-level retry policies.
 */
interface IntelligenceCommandRetryBoundary {
    /**
     * Evaluates whether an interaction failure is retryable.
     *
     * @param interaction The UI interaction that failed.
     * @param throwable The cause of failure.
     * @param currentAttempt The number of attempts already made.
     * @return The structured retry result.
     */
    fun evaluateRetry(
        interaction: IntelligenceUiInteraction,
        throwable: Throwable,
        currentAttempt: Int
    ): IntelligenceCommandRetryResult
}
