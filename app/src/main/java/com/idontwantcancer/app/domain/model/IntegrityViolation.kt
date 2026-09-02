package com.idontwantcancer.app.domain.model

/**
 * Represents a specific instance of an integrity failure in the intelligence lifecycle.
 */
data class IntegrityViolation(
    val category: IntegrityViolationCategory,
    val severity: IntegrityViolationSeverity,
    val affectedEntityId: String,
    val affectedEntityType: String,
    val description: String,
    val relatedEntityId: String? = null,
    val provenanceReference: String? = null
)
