package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryApplicationStateReconciliationResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the intelligence component responsible for handing off 
 * reconciliation results to the application architecture.
 */
interface IntelligenceReentryReconciliationHandoffBoundary {
    /**
     * Hands off a reconciliation result for observation by the application.
     *
     * @param result The result from the reconciliation boundary (Step 103).
     */
    suspend fun handoffResult(
        result: IntelligenceReentryApplicationStateReconciliationResult
    )

    /**
     * Provides a stream of reconciliation outcomes for observation.
     */
    val outcomeStream: Flow<IntelligenceReentryApplicationStateReconciliationResult>

    /**
     * Retrieves the latest reconciliation result for a specific re-entry identity.
     */
    fun getLatestResult(reentryIdentity: String): IntelligenceReentryApplicationStateReconciliationResult?
}
