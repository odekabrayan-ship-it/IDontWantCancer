package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the result of a re-entry deduplication check.
 */
@Serializable
enum class DeduplicationStatus {
    /**
     * This re-entry event has not been previously admitted.
     */
    NEW_REENTRY,

    /**
     * This exact re-entry event (intelligence + state) has already been admitted.
     */
    ALREADY_ADMITTED
}
