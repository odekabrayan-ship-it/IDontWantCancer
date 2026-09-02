package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import javax.inject.Inject

/**
 * Default implementation of the consumer isolation boundary. 
 * Orchestrates verification and admission to provide a single, safe read-only access point.
 */
class DefaultIntelligenceReentryIsolatedConsumerBoundary @Inject constructor(
    private val queryBoundary: IntelligenceReentryLifecycleQueryBoundary,
    private val admissionBoundary: IntelligenceReentryConsumerAdmissionBoundary
) : IntelligenceReentryIsolatedConsumerBoundary {

    override suspend fun getIsolatedContract(
        reentryIdentity: String,
        consumer: IntelligenceConsumerIdentity
    ): IntelligenceReentryHandoffConsumptionContract? {
        // 1. Query verified state through query boundary (Step 84)
        val contract = queryBoundary.getConsumptionContract(reentryIdentity) ?: return null

        // 2. Evaluate admission for the specific consumer (Step 87)
        val admissionResult = admissionBoundary.evaluateAdmission(contract, consumer)

        return if (admissionResult is IntelligenceConsumerAdmissionResult.Admitted) {
            // Return only the immutable data contract (Step 90)
            admissionResult.contract
        } else {
            // Denied or blocked
            null
        }
    }
}
