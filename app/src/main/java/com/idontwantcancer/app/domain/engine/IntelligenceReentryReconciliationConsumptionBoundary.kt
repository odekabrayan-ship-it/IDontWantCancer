package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryReconciliationConsumptionContract

/**
 * Interface for consuming the reconciled state consumption contract.
 * Provides downstream components with a verified view of application-state consistency.
 */
interface IntelligenceReentryReconciliationConsumptionBoundary {
    /**
     * Obtains the reconciliation consumption contract for a specific re-entry event.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @return The verified contract if available, or null.
     */
    suspend fun getReconciliationContract(
        reentryIdentity: String
    ): IntelligenceReentryReconciliationConsumptionContract?
}
