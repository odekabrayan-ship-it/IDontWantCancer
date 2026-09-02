package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured outcome of a consumer admission evaluation.
 */
@Serializable
sealed interface IntelligenceConsumerAdmissionResult {
    /**
     * The contract is authorized and permitted to enter the consumer.
     */
    @Serializable
    data class Admitted(
        val contract: IntelligenceReentryHandoffConsumptionContract,
        val consumer: IntelligenceConsumerIdentity,
        @Serializable(with = InstantSerializer::class)
        val admittedAt: Instant
    ) : IntelligenceConsumerAdmissionResult

    /**
     * The contract is rejected from entering the consumer.
     */
    @Serializable
    data class Rejected(
        val identity: String,
        val consumer: IntelligenceConsumerIdentity,
        val reason: ReentryAdmissionRejectionReason,
        @Serializable(with = InstantSerializer::class)
        val rejectedAt: Instant
    ) : IntelligenceConsumerAdmissionResult
}

/**
 * Defines structured reasons for consumer admission rejection.
 */
@Serializable
enum class ReentryAdmissionRejectionReason {
    /**
     * The consumption contract itself is invalid (Failed Step 86).
     */
    INVALID_CONSUMPTION,

    /**
     * The consumer is not authorized to receive this specific contract.
     */
    UNAUTHORIZED_CONSUMER,

    /**
     * The contract is structurally incompatible with the consumer's requirements.
     */
    INCOMPATIBLE_CONTRACT
}
