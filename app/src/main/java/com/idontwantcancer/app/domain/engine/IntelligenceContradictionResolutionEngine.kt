package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.EvidenceAssessment
import com.idontwantcancer.app.domain.model.IntelligenceConflict
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.model.SupersessionRelation

/**
 * Interface for the intelligence component responsible for identifying and
 * resolving disagreements between different pieces of evidence.
 */
interface IntelligenceContradictionResolutionEngine {
    /**
     * Analyzes a new evidence assessment against existing signals to detect and resolve contradictions.
     *
     * @param assessment The new evidence assessment to evaluate.
     * @param existingSignals The list of signals already in the agency's memory.
     * @param relations Any supersession relationships identified for the new intelligence.
     * @return A list of identified conflicts and their resolution status.
     */
    suspend fun analyzeContradictions(
        assessment: EvidenceAssessment,
        existingSignals: List<Signal>,
        relations: List<SupersessionRelation>
    ): List<IntelligenceConflict>
}
