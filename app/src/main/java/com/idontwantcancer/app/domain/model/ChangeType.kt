package com.idontwantcancer.app.domain.model

/**
 * Defines the nature of a detected change in source material.
 */
enum class ChangeType {
    NO_MEANINGFUL_CHANGE,
    NEW_INFORMATION,
    MATERIAL_CHANGE,
    CORRECTION,
    REVERSAL,
    STATUS_CHANGE,
    RECOMMENDATION_CHANGE,
    SAFETY_ACTION,
    EVIDENCE_CHANGE,
    CONFLICT_CHANGE
}
