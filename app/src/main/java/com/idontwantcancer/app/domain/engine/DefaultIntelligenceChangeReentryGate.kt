package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceChangeReentryGate] that applies 
 * deterministic structural rules to identify re-entry eligibility.
 */
class DefaultIntelligenceChangeReentryGate @Inject constructor() : IntelligenceChangeReentryGate {

    override fun evaluateReentry(
        diff: BriefingItemReconciliationDiff,
        currentSignal: Signal?
    ): IntelligenceReentryResult {
        val now = Instant.now()
        val triggers = mutableListOf<ReentryTrigger>()
        
        // 1. Identify Triggers
        if (diff.isSuperseded) triggers.add(ReentryTrigger.SUPERSESSION_CHANGE)
        if (diff.stateChanged) triggers.add(ReentryTrigger.STATE_CHANGE)
        if (diff.significanceChanged) triggers.add(ReentryTrigger.SIGNIFICANCE_CHANGE)
        if (diff.evidenceChanged) triggers.add(ReentryTrigger.SIGNIFICANT_EVIDENCE_CHANGE)
        if (diff.conflictChanged) triggers.add(ReentryTrigger.CONFLICT_CHANGE)

        // 2. Eligibility Decision
        val currentSignificance = currentSignal?.significanceLevel ?: SignificanceOutcome.NOT_SIGNIFICANT
        
        // Fail-Closed: Insignificant items do not re-enter for communication.
        if (currentSignificance == SignificanceOutcome.NOT_SIGNIFICANT && !diff.isSuperseded) {
            return IntelligenceReentryResult(
                intelligenceId = diff.intelligenceId,
                status = ReentryStatus.NO_REENTRY,
                triggers = triggers,
                reason = "Item is not significant enough for pipeline re-entry.",
                evaluatedAt = now
            )
        }

        val status = when {
            diff.isSuperseded || diff.stateChanged || diff.significanceChanged -> ReentryStatus.REENTRY_REQUIRED
            diff.evidenceChanged || diff.conflictChanged -> ReentryStatus.RE_EVALUATION_REQUIRED
            else -> ReentryStatus.NO_REENTRY
        }

        return IntelligenceReentryResult(
            intelligenceId = diff.intelligenceId,
            status = status,
            triggers = triggers,
            reason = deriveReason(status, triggers),
            evaluatedAt = now
        )
    }

    private fun deriveReason(status: ReentryStatus, triggers: List<ReentryTrigger>): String {
        return when (status) {
            ReentryStatus.REENTRY_REQUIRED -> "Material change detected in ${triggers.joinToString()}; re-entry into communication pipeline authorized."
            ReentryStatus.RE_EVALUATION_REQUIRED -> "Structural change detected in ${triggers.joinToString()}; re-evaluation of intelligence required."
            ReentryStatus.NO_REENTRY -> "No qualifying material changes detected."
        }
    }
}
