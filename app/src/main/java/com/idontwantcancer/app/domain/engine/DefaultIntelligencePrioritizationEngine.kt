package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import javax.inject.Inject

/**
 * Default implementation of [IntelligencePrioritizationEngine] that applies
 * deterministic rules to calculate presentation priority.
 */
class DefaultIntelligencePrioritizationEngine @Inject constructor() : IntelligencePrioritizationEngine {

    override fun prioritize(signals: List<Signal>): List<PrioritizedSignal> {
        return signals.map { signal ->
            val attentionLevel = calculateAttentionLevel(signal)
            val score = calculateRankingScore(signal)
            PrioritizedSignal(signal, attentionLevel, score)
        }.sortedWith(
            // Lowest ordinal (IMMEDIATE = 0) comes first
            compareBy<PrioritizedSignal> { it.attentionLevel }
                .thenByDescending { it.rankingScore }
                .thenByDescending { it.signal.publishedAt }
                .thenBy { it.signal.id }
        )
    }

    private fun calculateAttentionLevel(signal: Signal): AttentionLevel {
        return when (signal.importance) {
            SignalImportance.CRITICAL -> AttentionLevel.IMMEDIATE
            SignalImportance.HIGH -> AttentionLevel.IMPORTANT
            SignalImportance.MODERATE -> AttentionLevel.ROUTINE
            SignalImportance.LOW -> AttentionLevel.ARCHIVE
        }
    }

    /**
     * Calculates a deterministic ranking score based on Signal characteristics.
     * Higher score means higher presentation priority within an AttentionLevel.
     */
    private fun calculateRankingScore(signal: Signal): Int {
        var score = 0

        // 1. Evidence Confidence factor
        score += when (signal.confidence) {
            SignalConfidence.VERY_HIGH -> 40
            SignalConfidence.HIGH -> 30
            SignalConfidence.MODERATE -> 20
            SignalConfidence.LOW -> 10
        }

        // 2. Actionability factor
        if (signal.recommendedAction != null) {
            score += 15
        }

        // 3. Category weighting
        score += when (signal.category) {
            SignalCategory.SCREENING, SignalCategory.FOOD -> 10
            SignalCategory.CONSUMER_PRODUCTS, SignalCategory.ENVIRONMENT -> 8
            SignalCategory.REGULATION, SignalCategory.MEDICINE -> 6
            SignalCategory.PREVENTION, SignalCategory.RESEARCH -> 4
        }

        return score
    }
}
