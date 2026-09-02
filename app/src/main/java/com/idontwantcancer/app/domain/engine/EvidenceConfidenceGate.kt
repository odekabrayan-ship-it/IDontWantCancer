package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for determining how 
 * confidently the agency can characterize detected intelligence.
 */
interface EvidenceConfidenceGate {
    /**
     * Evaluates the confidence level of the evidence supporting a detected change.
     *
     * @param assessment The initial evidence assessment.
     * @param source The primary source of the intelligence.
     * @param conflicts Any identified conflicts related to this intelligence.
     * @return The final confidence decision.
     */
    fun evaluate(
        assessment: EvidenceAssessment,
        source: IntelligenceSource,
        conflicts: List<IntelligenceConflict>
    ): ConfidenceDecision
}
