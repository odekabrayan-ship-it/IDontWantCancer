package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the admission boundary that enforces 
 * structural authorization rules for downstream consumers.
 */
class DefaultIntelligenceReentryConsumerAdmissionBoundary @Inject constructor(
    private val consumptionIntegrityGate: IntelligenceReentryConsumptionIntegrityGate
) : IntelligenceReentryConsumerAdmissionBoundary {

    override fun evaluateAdmission(
        contract: IntelligenceReentryHandoffConsumptionContract,
        consumer: IntelligenceConsumerIdentity
    ): IntelligenceConsumerAdmissionResult {
        val now = Instant.now()

        // 1. Authoritative Validation First (Step 86)
        val validationResult = consumptionIntegrityGate.validateConsumption(contract)
        if (validationResult.status == ConsumptionValidationStatus.INVALID_CONSUMPTION) {
            return IntelligenceConsumerAdmissionResult.Rejected(
                identity = contract.reentryIdentity,
                consumer = consumer,
                reason = ReentryAdmissionRejectionReason.INVALID_CONSUMPTION,
                rejectedAt = now
            )
        }

        // 2. Structural Authorization Logic
        // In this implementation, any valid contract is permitted to enter 
        // the authorized domain consumers defined in IntelligenceConsumerIdentity.

        return IntelligenceConsumerAdmissionResult.Admitted(
            contract = contract,
            consumer = consumer,
            admittedAt = now
        )
    }
}
