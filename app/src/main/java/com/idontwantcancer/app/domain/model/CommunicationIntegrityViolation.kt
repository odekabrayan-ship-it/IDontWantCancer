package com.idontwantcancer.app.domain.model

/**
 * Represents a specific integrity failure within a communication package.
 */
data class CommunicationIntegrityViolation(
    val type: CommunicationIntegrityViolationType,
    val description: String,
    val expectedValue: String? = null,
    val actualValue: String? = null
)
