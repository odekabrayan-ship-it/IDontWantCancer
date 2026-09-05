package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Categorizes the type of directive that was completed.
 */
enum class HealingLogType {
    MANUAL,
    SYMPTOM
}

/**
 * Represents a permanent record of a completed healing directive.
 */
@Serializable
data class HealingLogEntry(
    val id: String,
    val directiveId: String,
    val directiveName: String,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant,
    val type: HealingLogType
)
