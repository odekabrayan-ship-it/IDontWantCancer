package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the eligibility status for an intelligence item to re-enter 
 * the communication pipeline after a reconciliation change.
 */
@Serializable
enum class ReentryStatus {
    /**
     * No material change detected; re-entry is not required.
     */
    NO_REENTRY,

    /**
     * Material change detected; item is eligible for fresh communication evaluation.
     */
    REENTRY_REQUIRED,

    /**
     * Structural change detected that requires fresh scientific or regulatory evaluation.
     */
    RE_EVALUATION_REQUIRED
}
