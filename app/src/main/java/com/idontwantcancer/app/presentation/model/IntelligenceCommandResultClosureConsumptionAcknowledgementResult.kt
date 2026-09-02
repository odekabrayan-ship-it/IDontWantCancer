package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of an authoritative acknowledgement 
 * for a consumed closure event.
 */
@Serializable
data class IntelligenceCommandResultClosureConsumptionAcknowledgementResult(
    val operationId: String,
    @Serializable(with = InstantSerializer::class)
    val acknowledgedAt: Instant = Instant.now(),
    val reason: String? = null
)
