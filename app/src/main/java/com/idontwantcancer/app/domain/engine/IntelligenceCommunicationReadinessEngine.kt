package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for determining 
 * communication readiness of intelligence items.
 */
interface IntelligenceCommunicationReadinessEngine {
    /**
     * Evaluates whether an intelligence item is ready for communication.
     *
     * @param signal The signal being evaluated.
     * @param synthesis The collective evidence synthesis.
     * @param gap The identified evidence gaps.
     * @param conflicts All active conflicts for the topic.
     * @return The readiness result.
     */
    fun evaluateReadiness(
        signal: Signal,
        synthesis: EvidenceSynthesisResult,
        gap: EvidenceGapResult,
        conflicts: List<IntelligenceConflict>
    ): CommunicationReadinessResult
}
