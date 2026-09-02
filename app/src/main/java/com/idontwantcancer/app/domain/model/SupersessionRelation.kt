package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents a relationship where one intelligence entry is superseded by another.
 */
data class SupersessionRelation(
    val id: String,
    val previousEntryId: String,
    val supersedingEntryId: String,
    val type: SupersessionType,
    val reason: String,
    val detectedAt: Instant
)
