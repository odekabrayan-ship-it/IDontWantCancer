package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * High-level status of the consumption integrity check.
 */
@Serializable
enum class ConsumptionValidationStatus {
    VALID_CONSUMPTION,
    INVALID_CONSUMPTION
}

/**
 * Structured reasons for consumption validation failures.
 */
@Serializable
enum class ConsumptionValidationError {
    MISSING_IDENTITY,
    INVALID_STATE,
    INCONSISTENT_TERMINAL_FLAG,
    STALE_CONTRACT,
    FUTURE_TIMESTAMP
}

/**
 * The structured outcome of a re-entry lifecycle consumption integrity check.
 */
@Serializable
data class IntelligenceReentryConsumptionValidationResult(
    val reentryIdentity: String,
    val status: ConsumptionValidationStatus,
    val errors: List<ConsumptionValidationError>,
    @Serializable(with = InstantSerializer::class)
    val validatedAt: Instant
)
