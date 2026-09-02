package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the transition completion boundary.
 * Provides a terminal status without initiating further mutations.
 */
class DefaultIntelligenceReentryTransitionCompletionBoundary @Inject constructor() : 
    IntelligenceReentryTransitionCompletionBoundary {

    override fun completeTransition(
        verificationResult: IntelligenceReentryPostTransitionVerificationResult
    ): IntelligenceReentryTransitionCompletionResult {
        val now = Instant.now()

        // 1. Completion Logic
        // Only a 'VERIFIED' status from Step 99 qualifies for 'COMPLETED'.
        val status = if (verificationResult.status == ReentryPostTransitionVerificationStatus.VERIFIED) {
            ReentryTransitionCompletionStatus.COMPLETED
        } else {
            ReentryTransitionCompletionStatus.NOT_COMPLETED
        }

        return IntelligenceReentryTransitionCompletionResult(
            reentryIdentity = verificationResult.reentryIdentity,
            status = status,
            verificationResult = verificationResult,
            completedAt = now
        )
    }
}
