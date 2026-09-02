package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.ReentryReconciliationUiState

/**
 * Boundary interface for projecting verified re-entry reconciled state into 
 * application-level read models.
 */
interface ReentryStateProjectionBoundary {
    /**
     * Projects a verified re-entry reconciliation into its presentation UI state.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @return The read-only UI projection, or null if not available.
     */
    suspend fun projectReconciliation(reentryIdentity: String): ReentryReconciliationUiState?
}
