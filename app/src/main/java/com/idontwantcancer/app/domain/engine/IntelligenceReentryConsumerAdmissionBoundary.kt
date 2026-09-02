package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceConsumerAdmissionResult
import com.idontwantcancer.app.domain.model.IntelligenceConsumerIdentity
import com.idontwantcancer.app.domain.model.IntelligenceReentryHandoffConsumptionContract

/**
 * Interface for the intelligence component responsible for determining 
 * whether validated lifecycle information is permitted to enter a specific consumer.
 */
interface IntelligenceReentryConsumerAdmissionBoundary {
    /**
     * Evaluates admission eligibility for a consumer.
     *
     * @param contract The validated consumption contract.
     * @param consumer The identity of the requesting consumer.
     * @return The structured admission result (Admitted or Rejected).
     */
    fun evaluateAdmission(
        contract: IntelligenceReentryHandoffConsumptionContract,
        consumer: IntelligenceConsumerIdentity
    ): IntelligenceConsumerAdmissionResult
}
