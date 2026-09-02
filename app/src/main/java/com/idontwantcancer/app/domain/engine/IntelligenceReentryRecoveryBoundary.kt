package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.ReentryRecoveryResult

/**
 * Interface for the intelligence component that acts as the secure boundary
 * for recovered re-entry lifecycle state.
 */
interface IntelligenceReentryRecoveryBoundary {
    /**
     * Attempts to retrieve a verified re-entry lifecycle state.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @return The verified recovery result (Verified or Unverified).
     */
    suspend fun getVerifiedReentry(reentryIdentity: String): ReentryRecoveryResult
}
