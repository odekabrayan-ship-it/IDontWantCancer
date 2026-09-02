package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * A structured collection of intelligence and context required for 
 * authoritative communication of an intelligence item.
 */
@Serializable
data class IntelligenceCommunicationPackage(
    val intelligenceId: String,
    val threadId: String,
    val changeId: String?,
    val currentState: ReconstructedState,
    val continuity: IntelligenceContinuityResult?,
    val narrative: IntelligenceNarrativeSequence,
    val significanceLevel: SignificanceOutcome?,
    val priority: AttentionLevel?,
    val evidenceSynthesis: EvidenceSynthesisResult,
    val confidence: SignalConfidence?,
    val conflicts: List<IntelligenceConflict>,
    val evidenceGap: EvidenceGapResult,
    val relevance: IntelligenceRelevanceResult?,
    val freshness: FreshnessStatus?,
    val communicationReadiness: CommunicationReadinessResult,
    val provenance: IntelligenceProvenance,
    @Serializable(with = InstantSerializer::class)
    val assembledAt: Instant
)
