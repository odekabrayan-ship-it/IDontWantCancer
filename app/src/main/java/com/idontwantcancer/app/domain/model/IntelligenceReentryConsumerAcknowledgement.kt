package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured report from a downstream consumer.
 */
@Serializable
data class IntelligenceReentryConsumerAcknowledgement(
    val reentryIdentity: String,
    val consumer: IntelligenceConsumerIdentity,
    val status: ReentryAcknowledgementStatus,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant,
    val reason: String? = null
)
