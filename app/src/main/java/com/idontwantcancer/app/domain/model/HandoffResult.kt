package com.idontwantcancer.app.domain.model

/**
 * Represents the structured result of attempting to cross the communication handoff boundary.
 */
sealed interface HandoffResult {
    /**
     * The intelligence successfully crossed the boundary.
     */
    data class Success(val handoff: IntelligenceCommunicationHandoff) : HandoffResult

    /**
     * The handoff failed due to safety or integrity violations.
     */
    data class Failure(
        val reason: String,
        val readinessLevel: CommunicationReadinessLevel,
        val integrityStatus: CommunicationIntegrityStatus? = null
    ) : HandoffResult
}
