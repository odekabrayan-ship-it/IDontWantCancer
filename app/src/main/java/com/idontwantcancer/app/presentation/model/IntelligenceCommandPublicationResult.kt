package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of an authoritative publication for a command result.
 */
@Serializable
data class IntelligenceCommandPublicationResult(
    val operationId: String,
    val status: CommandPublicationStatus,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val publishedAt: Instant = Instant.now()
)
