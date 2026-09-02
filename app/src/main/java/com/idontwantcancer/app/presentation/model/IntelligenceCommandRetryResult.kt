package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of a command retry evaluation.
 */
@Serializable
data class IntelligenceCommandRetryResult(
    val status: CommandRetryStatus,
    val reason: String? = null,
    val attemptCount: Int = 0,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant = Instant.now()
)
