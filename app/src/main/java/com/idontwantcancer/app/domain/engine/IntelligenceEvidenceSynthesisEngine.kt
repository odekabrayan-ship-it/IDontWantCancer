package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.EvidenceSynthesisResult
import com.idontwantcancer.app.domain.model.IntelligenceConflict
import com.idontwantcancer.app.domain.model.Signal

/**
 * Interface for the intelligence component responsible for determining 
 * what the available structured evidence collectively establishes.
 */
interface IntelligenceEvidenceSynthesisEngine {
    /**
     * Synthesizes evidence for an intelligence thread.
     *
     * @param threadId The unique identifier of the thread.
     * @param signals All signals associated with the thread.
     * @param conflicts All active conflicts related to the thread.
     * @return The synthesis result.
     */
    fun synthesizeEvidence(
        threadId: String,
        signals: List<Signal>,
        conflicts: List<IntelligenceConflict>
    ): EvidenceSynthesisResult
}
