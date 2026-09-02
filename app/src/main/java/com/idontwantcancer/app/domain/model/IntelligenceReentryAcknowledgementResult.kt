package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * The structured outcome of a re-entry lifecycle acknowledgement.
 */
@Serializable
data class IntelligenceReentryAcknowledgementResult(
    val acknowledgement: IntelligenceReentryConsumerAcknowledgement,
    val isRegistered: Boolean,
    val reason: String? = null
)
