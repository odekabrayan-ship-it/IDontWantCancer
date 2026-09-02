package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryHandoffConsumptionContract
import com.idontwantcancer.app.domain.model.ReentryHistoryResult
import com.idontwantcancer.app.domain.model.ReentryRecoveryResult

/**
 * Narrow, read-only domain interface for obtaining verified re-entry lifecycle information.
 * Enforces verification through Step 83 before exposing any historical or current state.
 */
interface IntelligenceReentryLifecycleQueryBoundary {
    /**
     * Retrieves the current verified lifecycle state for a re-entry event.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @return The verified recovery result.
     */
    suspend fun getVerifiedLifecycle(reentryIdentity: String): ReentryRecoveryResult

    /**
     * Retrieves the verified transition history for a re-entry event.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @return The verified history result.
     */
    suspend fun getVerifiedHistory(reentryIdentity: String): ReentryHistoryResult

    /**
     * Retrieves a narrow consumption contract for a verified re-entry event.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @return The consumption contract if verified, or null if unverified/not found.
     */
    suspend fun getConsumptionContract(reentryIdentity: String): IntelligenceReentryHandoffConsumptionContract?
}
