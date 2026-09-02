package com.idontwantcancer.app.domain.model

/**
 * Represents the outcome of the intelligence deduplication process.
 */
enum class DeduplicationType {
    /**
     * The intelligence record represents a genuinely new and unique event.
     */
    UNIQUE_EVENT,

    /**
     * The intelligence record is a confirmed duplicate of an existing event.
     */
    DUPLICATE_EVENT,

    /**
     * The record may be a duplicate, but the system cannot confirm it with enough certainty.
     */
    POSSIBLE_DUPLICATE
}

/**
 * The result of a deduplication attempt.
 */
data class DeduplicationResult(
    val type: DeduplicationType,
    val existingEventId: String? = null,
    val confidence: Float = 1.0f // 1.0 for deterministic matches
)
