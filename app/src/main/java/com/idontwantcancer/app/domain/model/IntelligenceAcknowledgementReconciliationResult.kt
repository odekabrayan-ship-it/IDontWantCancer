package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The structured result of reconciling a downstream acknowledgement with 
 * an authorized handoff event.
 */
@Serializable
data class IntelligenceAcknowledgementReconciliationResult(
    val reentryIdentity: String,
    val status: AcknowledgementReconciliationStatus,
    val consumer: IntelligenceConsumerIdentity,
    val mismatchReason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val reconciledAt: Instant
)
