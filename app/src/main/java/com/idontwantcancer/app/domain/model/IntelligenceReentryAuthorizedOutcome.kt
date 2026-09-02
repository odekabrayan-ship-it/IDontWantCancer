package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * An authorized representation of a reconciliation outcome, 
 * ready for consumption by orchestration.
 */
@Serializable
data class IntelligenceReentryAuthorizedOutcome(
    val outcome: IntelligenceReentryReconciliationOutcome,
    val isAuthorizedForOrchestration: Boolean,
    @Serializable(with = InstantSerializer::class)
    val authorizedAt: Instant
)
