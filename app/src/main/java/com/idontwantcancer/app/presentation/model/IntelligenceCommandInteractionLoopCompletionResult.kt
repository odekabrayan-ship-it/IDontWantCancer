package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured terminal result of an application command interaction loop.
 */
@Serializable
data class IntelligenceCommandInteractionLoopCompletionResult(
    val operationId: String,
    val finality: CommandConsumptionFinality,
    val isSynchronized: Boolean,
    @Serializable(with = InstantSerializer::class)
    val completedAt: Instant = Instant.now()
)
