package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryDecisionEligibilityResult
import com.idontwantcancer.app.domain.model.IntelligenceReentryDecisionRecord
import com.idontwantcancer.app.domain.model.IntelligenceReentryAuthorizedOutcome

/**
 * Interface for the component that produces immutable decision records
 * for the lifecycle authority.
 */
interface IntelligenceReentryDecisionRecordBoundary {
    /**
     * Creates a deterministic decision record from an eligibility result.
     *
     * @param authorizedOutcome The authorized outcome context.
     * @param eligibilityResult The result of the eligibility gate (Step 95).
     * @return The immutable decision record.
     */
    fun createRecord(
        authorizedOutcome: IntelligenceReentryAuthorizedOutcome,
        eligibilityResult: IntelligenceReentryDecisionEligibilityResult
    ): IntelligenceReentryDecisionRecord
}
