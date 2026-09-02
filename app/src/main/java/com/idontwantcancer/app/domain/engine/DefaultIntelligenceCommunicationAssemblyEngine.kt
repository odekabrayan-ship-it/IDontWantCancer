package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceCommunicationAssemblyEngine] that 
 * bundles structured facts and context for downstream communication.
 */
class DefaultIntelligenceCommunicationAssemblyEngine @Inject constructor() : IntelligenceCommunicationAssemblyEngine {

    override fun assemblePackage(
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
    ): IntelligenceCommunicationPackage {
        return IntelligenceCommunicationPackage(
            intelligenceId = signal.id,
            threadId = thread.id,
            changeId = currentState.lastMeaningfulChangeId,
            currentState = currentState,
            continuity = continuity,
            narrative = narrative,
            significanceLevel = signal.significanceLevel,
            priority = prioritized?.attentionLevel,
            evidenceSynthesis = synthesis,
            confidence = signal.confidence,
            conflicts = conflicts,
            evidenceGap = gap,
            relevance = relevance,
            freshness = freshness,
            communicationReadiness = readiness,
            provenance = provenance,
            assembledAt = Instant.now()
        )
    }
}
