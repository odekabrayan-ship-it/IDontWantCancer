package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines structured triggers for intelligence pipeline re-entry.
 */
@Serializable
enum class ReentryTrigger {
    STATE_CHANGE,
    SUPERSESSION_CHANGE,
    SIGNIFICANT_EVIDENCE_CHANGE,
    CONFLICT_CHANGE,
    UNCERTAINTY_CHANGE,
    SCOPE_CHANGE,
    SIGNIFICANCE_CHANGE
}
