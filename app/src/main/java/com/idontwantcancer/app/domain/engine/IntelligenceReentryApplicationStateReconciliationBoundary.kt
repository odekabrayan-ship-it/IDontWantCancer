package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryApplicationStateReconciliationResult
import com.idontwantcancer.app.domain.model.IntelligenceReentryTransitionCompletionResult

/**
 * Interface for the component responsible for determining whether the 
 * existing application state properly reflects a re-entry completion.
 */
interface IntelligenceReentryApplicationStateReconciliationBoundary {
    /**
     * Reconciles a completed re-entry transition with current application state.
     *
     * @param completion The terminal completion result from Step 100.
     * @return The structured reconciliation result.
     */
    suspend fun reconcileWithApplication(
        completion: IntelligenceReentryTransitionCompletionResult
    ): IntelligenceReentryApplicationStateReconciliationResult
}
