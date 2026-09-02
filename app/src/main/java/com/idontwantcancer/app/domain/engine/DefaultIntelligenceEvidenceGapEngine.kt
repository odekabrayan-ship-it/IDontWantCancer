package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceEvidenceGapEngine] that identifies
 * evidence gaps based on synthesis levels and unresolved contradictions.
 */
class DefaultIntelligenceEvidenceGapEngine @Inject constructor() : IntelligenceEvidenceGapEngine {

    override fun analyzeGaps(
        thread: IntelligenceThread,
        synthesis: EvidenceSynthesisResult,
        conflicts: List<IntelligenceConflict>
    ): EvidenceGapResult {
        val now = Instant.now()

        // 1. Conflict Gap: Unresolved conflicts imply a gap in resolving evidence.
        val unresolvedConflict = conflicts.find { it.resolutionStatus == ResolutionStatus.UNRESOLVED }
        if (unresolvedConflict != null) {
            return EvidenceGapResult(
                threadId = thread.id,
                level = EvidenceGapLevel.UNRESOLVED_CONFLICT,
                reason = "A significant conflict exists between authoritative sources that requires resolving evidence.",
                relatedConflictId = unresolvedConflict.id,
                analyzedAt = now
            )
        }

        // 2. Corroboration Gap: High significance but limited independent sources.
        // (Conceptual: thread context doesn't currently store 'maxSignificance' of its signals easily,
        // we use synthesis level as a proxy).
        if (synthesis.level == EvidenceSynthesisLevel.SUPPORTED && synthesis.contributingSignalIds.size < 2) {
             return EvidenceGapResult(
                threadId = thread.id,
                level = EvidenceGapLevel.MISSING_CORROBORATION,
                reason = "Current intelligence is supported by a single source and lacks independent corroboration.",
                analyzedAt = now
            )
        }

        // 3. Support Gap: Synthesis level is too low for an active thread.
        if (synthesis.level == EvidenceSynthesisLevel.INSUFFICIENT || synthesis.level == EvidenceSynthesisLevel.LIMITED) {
            return EvidenceGapResult(
                threadId = thread.id,
                level = EvidenceGapLevel.INSUFFICIENT_SUPPORT,
                reason = "Available evidence is preliminary or insufficient to establish a firm intelligence state.",
                analyzedAt = now
            )
        }

        return EvidenceGapResult(
            threadId = thread.id,
            level = EvidenceGapLevel.NO_IDENTIFIED_GAP,
            reason = "The current evidence picture appears structurally complete for this topic.",
            analyzedAt = now
        )
    }
}
