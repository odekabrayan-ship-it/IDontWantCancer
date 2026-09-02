package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the structured outcome of the reconciliation process, 
 * defining what the system knows about the correspondence between 
 * consumption and acknowledgement.
 */
@Serializable
data class IntelligenceReentryReconciliationOutcome(
    val reentryIdentity: String,
    val status: AcknowledgementReconciliationStatus,
    val reason: String? = null
)
