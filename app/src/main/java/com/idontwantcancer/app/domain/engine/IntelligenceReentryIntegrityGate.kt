package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryIntegrityResult

/**
 * Interface for the intelligence component responsible for verifying the 
 * internal consistency of persisted re-entry lifecycle state.
 */
interface IntelligenceReentryIntegrityGate {
    /**
     * Verifies the integrity of a re-entry event by reconciling its 
     * stored state with its audit history.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @return The structured integrity and recovery result.
     */
    suspend fun verifyIntegrity(reentryIdentity: String): IntelligenceReentryIntegrityResult
}
