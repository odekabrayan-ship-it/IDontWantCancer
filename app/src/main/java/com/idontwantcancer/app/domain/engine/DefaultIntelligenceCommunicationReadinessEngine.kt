package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceCommunicationReadinessEngine] that applies 
 * deterministic domain rules to determine if intelligence is ready for the communication layer.
 */
class DefaultIntelligenceCommunicationReadinessEngine @Inject constructor() : IntelligenceCommunicationReadinessEngine {

    override fun evaluateReadiness(
        signal: Signal,
        synthesis: EvidenceSynthesisResult,
        gap: EvidenceGapResult,
        conflicts: List<IntelligenceConflict>
    ): CommunicationReadinessResult {
        val now = Instant.now()

        // 1. Conflict Check (Step 53)
        val hasUnresolvedConflict = conflicts.any { it.resolutionStatus == ResolutionStatus.UNRESOLVED }
        if (hasUnresolvedConflict) {
            return CommunicationReadinessResult(
                level = CommunicationReadinessLevel.CONFLICTED,
                reason = "Intelligence is contested by authoritative sources.",
                requiresQualification = true,
                evaluatedAt = now
            )
        }

        // 2. Gap Check (Step 56)
        if (gap.level == EvidenceGapLevel.MISSING_SCOPE_COVERAGE || gap.level == EvidenceGapLevel.INSUFFICIENT_SUPPORT) {
             return CommunicationReadinessResult(
                level = CommunicationReadinessLevel.INCOMPLETE,
                reason = "Intelligence lacks critical supporting evidence or scope coverage.",
                requiresQualification = true,
                evaluatedAt = now
            )
        }

        // 3. Synthesis and Confidence Check
        if (synthesis.level == EvidenceSynthesisLevel.CONTESTED) {
            return CommunicationReadinessResult(
                level = CommunicationReadinessLevel.READY_WITH_UNCERTAINTY,
                reason = "Intelligence is significant but subject to ongoing scientific discussion.",
                requiresQualification = true,
                evaluatedAt = now
            )
        }

        if (signal.confidence == SignalConfidence.LOW || synthesis.level == EvidenceSynthesisLevel.LIMITED) {
            return CommunicationReadinessResult(
                level = CommunicationReadinessLevel.READY_WITH_UNCERTAINTY,
                reason = "Intelligence is based on preliminary or limited evidence.",
                requiresQualification = true,
                evaluatedAt = now
            )
        }

        // 4. Default: Ready
        return CommunicationReadinessResult(
            level = CommunicationReadinessLevel.READY,
            reason = "Intelligence is structured and well-supported.",
            requiresQualification = false,
            evaluatedAt = now
        )
    }
}
