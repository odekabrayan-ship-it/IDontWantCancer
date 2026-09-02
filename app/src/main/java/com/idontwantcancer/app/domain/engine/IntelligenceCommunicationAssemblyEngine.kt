package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for assembling 
 * all required facts and context into a structured communication package.
 */
interface IntelligenceCommunicationAssemblyEngine {
    /**
     * Assembles a communication package for a specific intelligence item.
     *
     * @param signal The primary signal to communicate.
     * @param thread The thread context.
     * @param currentState The authoritative current understanding.
     * @param continuity The longitudinal continuity result.
     * @param narrative The structured narrative sequence.
     * @param synthesis The collective evidence synthesis.
     * @param gap The identified evidence gaps.
     * @param readiness The communication readiness result.
     * @param provenance The complete auditable lineage.
     * @param conflicts Related conflicts if any.
     * @param prioritized The prioritization metadata.
     * @param relevance The relevance evaluation result.
     * @param freshness The temporal freshness status.
     * @return The assembled communication package.
     */
    fun assemblePackage(
        signal: Signal,
        thread: IntelligenceThread,
        currentState: ReconstructedState,
        continuity: IntelligenceContinuityResult?,
        narrative: IntelligenceNarrativeSequence,
        synthesis: EvidenceSynthesisResult,
        gap: EvidenceGapResult,
        readiness: CommunicationReadinessResult,
        provenance: IntelligenceProvenance,
        conflicts: List<IntelligenceConflict>,
        prioritized: PrioritizedSignal?,
        relevance: IntelligenceRelevanceResult?,
        freshness: FreshnessStatus?
    ): IntelligenceCommunicationPackage
}
