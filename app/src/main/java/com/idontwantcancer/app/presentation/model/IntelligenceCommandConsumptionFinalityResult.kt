package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of a downstream consumption finality evaluation.
 */
@Serializable
data class IntelligenceCommandConsumptionFinalityResult(
    val operationId: String,
    val finality: CommandConsumptionFinality,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant = Instant.now()
)
