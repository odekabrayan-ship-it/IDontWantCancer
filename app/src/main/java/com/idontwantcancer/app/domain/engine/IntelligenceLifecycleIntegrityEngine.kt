package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceIntegrityResult

/**
 * Interface for the intelligence component responsible for verifying 
 * the internal consistency and referential integrity of the intelligence lifecycle.
 */
interface IntelligenceLifecycleIntegrityEngine {
    /**
     * Verifies the integrity of all intelligence currently in memory for a specific topic.
     *
     * @param threadId The unique identifier of the thread/topic to check.
     * @return The structured integrity result.
     */
    suspend fun verifyThreadIntegrity(threadId: String): IntelligenceIntegrityResult

    /**
     * Performs a global referential integrity check across the entire intelligence memory.
     * @return The structured integrity result.
     */
    suspend fun verifyGlobalIntegrity(): IntelligenceIntegrityResult
}
