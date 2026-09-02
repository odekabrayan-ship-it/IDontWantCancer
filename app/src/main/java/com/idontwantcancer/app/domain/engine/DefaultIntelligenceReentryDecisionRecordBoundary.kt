package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the decision record boundary.
 * Maps eligibility results to deterministic decision candidates.
 */
class DefaultIntelligenceReentryDecisionRecordBoundary @Inject constructor() : 
    IntelligenceReentryDecisionRecordBoundary {

    override fun createRecord(
        authorizedOutcome: IntelligenceReentryAuthorizedOutcome,
        eligibilityResult: IntelligenceReentryDecisionEligibilityResult
    ): IntelligenceReentryDecisionRecord {
        val now = Instant.now()
        val isEligible = eligibilityResult.status == IntelligenceReentryDecisionEligibilityStatus.ELIGIBLE_FOR_CONSIDERATION
        
        val status = if (isEligible) {
            DecisionRecordStatus.CANDIDATE_PRESENTED
        } else {
            DecisionRecordStatus.INELIGIBLE_FOR_DECISION
        }

        // Deterministic mapping: 
        // A reconciled and eligible outcome proposes the COMPLETED state.
        val proposedState = if (isEligible && authorizedOutcome.outcome.status == AcknowledgementReconciliationStatus.RECONCILED) {
            ReentryLifecycleState.COMPLETED
        } else {
            null
        }

        return IntelligenceReentryDecisionRecord(
            reentryIdentity = eligibilityResult.reentryIdentity,
            status = status,
            proposedState = proposedState,
            eligibilityResult = eligibilityResult,
            recordedAt = now
        )
    }
}
