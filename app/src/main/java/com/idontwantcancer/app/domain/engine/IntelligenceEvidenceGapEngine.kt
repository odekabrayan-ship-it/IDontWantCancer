package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.EvidenceGapResult
import com.idontwantcancer.app.domain.model.EvidenceSynthesisResult
import com.idontwantcancer.app.domain.model.IntelligenceConflict
import com.idontwantcancer.app.domain.model.IntelligenceThread

/**
 * Interface for the intelligence component responsible for identifying 
 * structural gaps in the current evidence picture.
 */
interface IntelligenceEvidenceGapEngine {
    /**
     * Analyzes the current state of an intelligence thread to identify missing information.
     *
     * @param thread The thread being analyzed.
     * @param synthesis The collective evidence synthesis for the thread.
     * @param conflicts All active conflicts for the thread.
     * @return The identified evidence gap, if any.
     */
    fun analyzeGaps(
        thread: IntelligenceThread,
        synthesis: EvidenceSynthesisResult,
        conflicts: List<IntelligenceConflict>
    ): EvidenceGapResult
}
