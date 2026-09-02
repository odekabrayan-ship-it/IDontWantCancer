package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryHandoffConsumptionContract
import com.idontwantcancer.app.domain.model.IntelligenceReentryConsumptionValidationResult

/**
 * Interface for the intelligence component responsible for validating that
 * a downstream consumer is operating on a valid re-entry lifecycle contract.
 */
interface IntelligenceReentryConsumptionIntegrityGate {
    /**
     * Validates a re-entry consumption contract.
     *
     * @param contract The contract to validate.
     * @return The structured validation result.
     */
    fun validateConsumption(
        contract: IntelligenceReentryHandoffConsumptionContract
    ): IntelligenceReentryConsumptionValidationResult
}
