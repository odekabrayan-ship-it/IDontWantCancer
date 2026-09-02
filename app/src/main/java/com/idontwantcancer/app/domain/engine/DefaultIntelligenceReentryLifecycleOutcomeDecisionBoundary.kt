package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the outcome decision boundary.
 * Applies deterministic rules to determine if a reconciled outcome qualifies 
 * for lifecycle consideration.
 */
class DefaultIntelligenceReentryLifecycleOutcomeDecisionBoundary @Inject constructor() : 
    IntelligenceReentryLifecycleOutcomeDecisionBoundary {

    override fun evaluateEligibility(
        authorizedOutcome: IntelligenceReentryAuthorizedOutcome
    ): IntelligenceReentryDecisionEligibilityResult {
        val now = Instant.now()
        val outcome = authorizedOutcome.outcome

        // 1. Authorization Requirement
        // Step 94 must have authorized the outcome for orchestration.
        if (!authorizedOutcome.isAuthorizedForOrchestration) {
            return IntelligenceReentryDecisionEligibilityResult(
                reentryIdentity = outcome.reentryIdentity,
                status = IntelligenceReentryDecisionEligibilityStatus.NOT_ELIGIBLE_FOR_CONSIDERATION,
                reason = "Outcome is not authorized for orchestration.",
                evaluatedAt = now
            )
        }

        // 2. Reconciliation Requirement
        // Only successfully reconciled events (acknowledgement matches handoff) 
        // are eligible for final lifecycle consideration in this boundary.
        val status = if (outcome.status == AcknowledgementReconciliationStatus.RECONCILED) {
            IntelligenceReentryDecisionEligibilityStatus.ELIGIBLE_FOR_CONSIDERATION
        } else {
            IntelligenceReentryDecisionEligibilityStatus.NOT_ELIGIBLE_FOR_CONSIDERATION
        }

        return IntelligenceReentryDecisionEligibilityResult(
            reentryIdentity = outcome.reentryIdentity,
            status = status,
            reason = if (status == IntelligenceReentryDecisionEligibilityStatus.ELIGIBLE_FOR_CONSIDERATION) 
                         "Reconciliation successfully verified; eligible for lifecycle authority."
                     else "Reconciliation mismatch detected; ineligible for standard lifecycle consideration.",
            evaluatedAt = now
        )
    }
}
