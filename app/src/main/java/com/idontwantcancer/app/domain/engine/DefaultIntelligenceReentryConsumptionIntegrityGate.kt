package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the consumption integrity gate that applies 
 * deterministic structural validation rules.
 */
class DefaultIntelligenceReentryConsumptionIntegrityGate @Inject constructor() : 
    IntelligenceReentryConsumptionIntegrityGate {

    override fun validateConsumption(
        contract: IntelligenceReentryHandoffConsumptionContract
    ): IntelligenceReentryConsumptionValidationResult {
        val now = Instant.now()
        val errors = mutableListOf<ConsumptionValidationError>()

        // 1. Identity Presence
        if (contract.reentryIdentity.isBlank() || contract.intelligenceId.isBlank()) {
            errors.add(ConsumptionValidationError.MISSING_IDENTITY)
        }

        // 2. Terminal Flag Consistency
        val expectedTerminal = isTerminal(contract.verifiedState)
        if (contract.isTerminal != expectedTerminal) {
            errors.add(ConsumptionValidationError.INCONSISTENT_TERMINAL_FLAG)
        }

        // 3. Timestamp Sanity
        if (contract.admittedAt.isAfter(now.plusSeconds(60))) { // Tolerance for clock skew
            errors.add(ConsumptionValidationError.FUTURE_TIMESTAMP)
        }

        val status = if (errors.isEmpty()) {
            ConsumptionValidationStatus.VALID_CONSUMPTION
        } else {
            ConsumptionValidationStatus.INVALID_CONSUMPTION
        }

        return IntelligenceReentryConsumptionValidationResult(
            reentryIdentity = contract.reentryIdentity,
            status = status,
            errors = errors,
            validatedAt = now
        )
    }

    private fun isTerminal(state: ReentryLifecycleState): Boolean {
        return state == ReentryLifecycleState.COMPLETED || 
               state == ReentryLifecycleState.REJECTED || 
               state == ReentryLifecycleState.SUPERSEDED
    }
}
