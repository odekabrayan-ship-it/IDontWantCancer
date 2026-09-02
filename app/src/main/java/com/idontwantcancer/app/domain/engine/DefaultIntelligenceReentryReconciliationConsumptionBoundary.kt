package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryReconciliationConsumptionContract
import javax.inject.Inject

/**
 * Default implementation of the reconciliation consumption boundary.
 * Provides downstream components with an immutable contract derived from verified state.
 */
class DefaultIntelligenceReentryReconciliationConsumptionBoundary @Inject constructor(
    private val handoffBoundary: IntelligenceReentryReconciliationHandoffBoundary
) : IntelligenceReentryReconciliationConsumptionBoundary {

    override suspend fun getReconciliationContract(
        reentryIdentity: String
    ): IntelligenceReentryReconciliationConsumptionContract? {
        // Leverages the authoritative handoff boundary's state (Step 104)
        return handoffBoundary.getLatestResult(reentryIdentity)?.toConsumptionContract()
    }
}
