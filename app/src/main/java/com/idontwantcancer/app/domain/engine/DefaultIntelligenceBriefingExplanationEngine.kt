package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceBriefingExplanationEngine] that uses 
 * deterministic templates to explain structured intelligence.
 */
class DefaultIntelligenceBriefingExplanationEngine @Inject constructor(
    private val memory: IntelligenceMemoryRepository
) : IntelligenceBriefingExplanationEngine {

    override suspend fun explain(
        item: IntelligenceBriefingItem,
        signal: Signal
    ): BriefingExplanation {
        // 1. Determine Change Nature (from Timeline)
        val entries = memory.getTimelineEntriesForThread(item.threadId)
        val changeEntry = entries.find { it.signalId == signal.id }
        
        val whatChanged = explainChangeNature(changeEntry?.type)

        // 2. Explain Significance
        val whyItMatters = explainSignificance(signal.significanceLevel)

        // 3. Explain Evidence Status
        val evidenceStatus = explainEvidenceConfidence(signal.confidence)

        // 4. Preserve Uncertainty
        val uncertainty = deriveUncertainty(signal)

        // 5. Conflict Summary
        val conflictSummary = if (signal.conflictStatus == ResolutionStatus.UNRESOLVED) {
            "Authoritative sources currently provide differing information on this specific development."
        } else null

        return BriefingExplanation(
            whatChanged = whatChanged,
            whyItMatters = whyItMatters,
            evidenceStatus = evidenceStatus,
            uncertainty = uncertainty,
            conflictSummary = conflictSummary
        )
    }

    private fun explainChangeNature(type: TimelineEntryType?): String {
        return when (type) {
            TimelineEntryType.REGULATORY_ACTION -> "An official regulatory or public health action has been taken."
            TimelineEntryType.REVERSAL -> "A previous recommendation or established finding has been reversed."
            TimelineEntryType.RECOMMENDATION_CHANGE -> "Official guidance or recommendations have changed."
            TimelineEntryType.CORRECTION -> "Information previously issued by the source has been formally corrected."
            TimelineEntryType.MATERIAL_CHANGE -> "A material change was detected in the scientific evidence or reporting."
            TimelineEntryType.EVIDENCE_UPGRADE -> "New evidence has been discovered that strengthens the understanding of this topic."
            TimelineEntryType.INITIAL_OBSERVATION -> "A new intelligence topic has been identified for tracking."
            else -> "A change in intelligence state has been detected."
        }
    }

    private fun explainSignificance(outcome: SignificanceOutcome?): String {
        return when (outcome) {
            SignificanceOutcome.CRITICAL -> "This represents a critical intelligence event with broad public health implications."
            SignificanceOutcome.HIGH_SIGNIFICANCE -> "This is a high-significance change that materially impacts the established understanding of this topic."
            SignificanceOutcome.SIGNIFICANT -> "This is a meaningful update to the current intelligence state."
            SignificanceOutcome.NOT_SIGNIFICANT -> "This update contains minor refinements to existing information."
            null -> "The agency has identified this as a worthwhile intelligence update."
        }
    }

    private fun explainEvidenceConfidence(confidence: SignalConfidence): String {
        return when (confidence) {
            SignalConfidence.VERY_HIGH -> "The agency has very high confidence in this conclusion, supported by independent corroboration from multiple authoritative sources."
            SignalConfidence.HIGH -> "This conclusion is supported by strong evidence from authoritative sources."
            SignalConfidence.MODERATE -> "The evidence supporting this conclusion is developing and requires further observation."
            SignalConfidence.LOW -> "The evidence for this conclusion is currently limited or preliminary."
        }
    }

    private fun deriveUncertainty(signal: Signal): String? {
        val factors = mutableListOf<String>()
        
        if (signal.confidence == SignalConfidence.LOW) {
            factors.add("Evidence is preliminary and subject to change as more data is acquired.")
        }
        
        if (signal.conflictStatus == ResolutionStatus.UNRESOLVED) {
            factors.add("Competing authoritative interpretations exist.")
        }

        return if (factors.isNotEmpty()) factors.joinToString(" ") else null
    }
}
