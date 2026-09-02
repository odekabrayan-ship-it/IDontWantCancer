package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import java.util.*
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceBriefingSelectionEngine] that applies
 * population-level filtering and ordering rules.
 */
class DefaultIntelligenceBriefingSelectionEngine @Inject constructor() : IntelligenceBriefingSelectionEngine {

    override fun selectBriefing(
        candidates: List<Pair<PrioritizedSignal, IntelligenceCommunicationPackage>>
    ): IntelligenceBriefingSelection {
        val now = Instant.now()
        val selectedPackages = mutableMapOf<String, IntelligenceCommunicationPackage>()
        val excludedItems = mutableMapOf<String, SelectionExclusionReason>()

        // 1. Eligibility Filtering
        val eligible = candidates.filter { (prioritized, pkg) ->
            val signal = prioritized.signal
            
            // Significance Check
            if (signal.significanceLevel == SignificanceOutcome.NOT_SIGNIFICANT) {
                excludedItems[signal.id] = SelectionExclusionReason.NOT_SIGNIFICANT
                return@filter false
            }

            // Readiness Check
            if (pkg.communicationReadiness.level == CommunicationReadinessLevel.NOT_READY) {
                excludedItems[signal.id] = SelectionExclusionReason.NOT_COMMUNICATION_READY
                return@filter false
            }

            // Freshness Check (Conceptual: excluding very old historical info if desired)
            if (pkg.freshness?.level == FreshnessLevel.STALE) {
                excludedItems[signal.id] = SelectionExclusionReason.SUPERSEDED
                return@filter false
            }

            true
        }

        // 2. Thread Consolidation (Keep highest priority per thread)
        val threadToBest = mutableMapOf<String, Pair<PrioritizedSignal, IntelligenceCommunicationPackage>>()
        for (item in eligible) {
            val (prioritized, pkg) = item
            val threadId = pkg.threadId
            
            val existing = threadToBest[threadId]
            if (existing == null || comparePriority(prioritized, existing.first) < 0) {
                if (existing != null) {
                    excludedItems[existing.first.signal.id] = SelectionExclusionReason.DUPLICATE
                }
                threadToBest[threadId] = item
            } else {
                excludedItems[prioritized.signal.id] = SelectionExclusionReason.DUPLICATE
            }
        }

        // 3. Deterministic Ordering
        val sortedFinal = threadToBest.values.sortedWith(
            compareBy<Pair<PrioritizedSignal, IntelligenceCommunicationPackage>> { it.first.attentionLevel.ordinal }
                .thenByDescending { it.first.signal.significanceLevel?.ordinal ?: -1 }
                .thenBy { it.first.signal.id } // Final tie-breaker
        ).take(MAX_BRIEFING_ITEMS)

        sortedFinal.forEach { (_, pkg) ->
            selectedPackages[pkg.intelligenceId] = pkg
        }

        return IntelligenceBriefingSelection(
            id = UUID.randomUUID().toString(),
            orderedSignalIds = sortedFinal.map { it.second.intelligenceId },
            selectedPackages = selectedPackages,
            excludedSignals = excludedItems,
            selectedAt = now
        )
    }

    private fun comparePriority(a: PrioritizedSignal, b: PrioritizedSignal): Int {
        return a.attentionLevel.ordinal.compareTo(b.attentionLevel.ordinal)
    }

    private companion object {
        private const val MAX_BRIEFING_ITEMS = 5
    }
}
