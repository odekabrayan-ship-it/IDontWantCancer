package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryAuthorizedOutcome
import com.idontwantcancer.app.domain.model.IntelligenceReentryDecisionEligibilityResult

/**
 * Interface for the intelligence component responsible for determining 
 * whether a reconciled outcome is eligible for consideration by lifecycle orchestration.
 */
interface IntelligenceReentryLifecycleOutcomeDecisionBoundary {
    /**
     * Evaluates the decision eligibility of an authorized reconciliation outcome.
     *
     * @param authorizedOutcome The authorized outcome from Step 94.
     * @return The structured eligibility result.
     */
    fun evaluateEligibility(
        authorizedOutcome: IntelligenceReentryAuthorizedOutcome
    ): IntelligenceReentryDecisionEligibilityResult
}
