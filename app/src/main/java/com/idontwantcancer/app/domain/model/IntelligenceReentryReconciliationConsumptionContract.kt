package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * An immutable consumption contract representing the verified 
 * reconciliation status of a re-entry event.
 * Ensures that application components can read consistency facts without acquiring authority.
 */
@Serializable
data class IntelligenceReentryReconciliationConsumptionContract(
    val reentryIdentity: String,
    val isConsistent: Boolean,
    @Serializable(with = InstantSerializer::class)
    val verifiedAt: Instant,
    val detail: String? = null
)
