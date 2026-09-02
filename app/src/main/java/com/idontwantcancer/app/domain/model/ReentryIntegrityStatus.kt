package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the high-level result of a re-entry lifecycle integrity check.
 */
@Serializable
enum class ReentryIntegrityStatus {
    /**
     * Stored lifecycle state is perfectly consistent with the audit trail.
     */
    INTEGRITY_CONFIRMED,

    /**
     * Inconsistencies detected between the stored state and audit history.
     */
    INTEGRITY_FAILED
}
