package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the eligibility for a reconciled outcome to be presented to the 
 * lifecycle authority for consideration.
 */
@Serializable
enum class IntelligenceReentryDecisionEligibilityStatus {
    /**
     * The outcome is eligible for consideration by the lifecycle authority.
     */
    ELIGIBLE_FOR_CONSIDERATION,

    /**
     * The outcome does not qualify for lifecycle consideration at this stage.
     */
    NOT_ELIGIBLE_FOR_CONSIDERATION
}
