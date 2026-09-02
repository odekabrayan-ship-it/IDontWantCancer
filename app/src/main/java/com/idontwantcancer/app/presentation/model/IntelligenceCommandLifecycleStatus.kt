package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the current status of an application command within its operational lifecycle.
 */
@Serializable
data class IntelligenceCommandLifecycleStatus(
    val interaction: IntelligenceUiInteraction,
    val stage: CommandLifecycleStage,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant
)
