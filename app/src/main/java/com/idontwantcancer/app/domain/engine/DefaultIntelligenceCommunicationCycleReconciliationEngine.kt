package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceCommunicationCycleReconciliationEngine] that 
 * applies deterministic comparison rules using the agency's memory.
 */
class DefaultIntelligenceCommunicationCycleReconciliationEngine @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val stateReconstructor: IntelligenceStateReconstructionEngine,
    private val synthesisEngine: IntelligenceEvidenceSynthesisEngine
) : IntelligenceCommunicationCycleReconciliationEngine {

    override suspend fun reconcileCycle(
        historicalBriefing: IntelligenceBriefing
    ): IntelligenceBriefingReconciliationResult {
        val now = Instant.now()
        val itemDiffs = mutableListOf<BriefingItemReconciliationDiff>()

        for (item in historicalBriefing.items) {
            val handoff = historicalBriefing.handoffs[item.intelligenceId] ?: continue
            val historicalPkg = handoff.communicationPackage
            
            // 1. Fetch Current State from Domain
            val currentThread = memory.getThreadById(item.threadId)
            val currentTimelineEntries = memory.getTimelineEntriesForThread(item.threadId)
            val currentTimeline = IntelligenceEventTimeline(item.threadId, currentThread?.topicIdentifier ?: "", currentTimelineEntries)
            val currentState = stateReconstructor.reconstructCurrentState(currentTimeline)
            
            val currentSignal = memory.getSignalById(item.intelligenceId)
            
            val threadSignals = currentTimelineEntries.mapNotNull { it.signalId }.mapNotNull { memory.getSignalById(it) }
            val currentConflicts = memory.getConflictsForTopic(currentThread?.topicIdentifier ?: "")
            val currentSynthesis = synthesisEngine.synthesizeEvidence(item.threadId, threadSignals, currentConflicts)

            // 2. Identify Differences
            val stateChanged = currentState.effectiveEntryId != historicalPkg.currentState.effectiveEntryId
            val significanceChanged = currentSignal?.significanceLevel != historicalPkg.significanceLevel
            val priorityChanged = currentSignal?.importance != historicalPkg.provenance.signal.importance // Importance as proxy for base priority
            val evidenceChanged = currentSynthesis.level != historicalPkg.evidenceSynthesis.level
            val conflictChanged = currentConflicts.any { it.resolutionStatus == ResolutionStatus.UNRESOLVED } != 
                                 historicalPkg.conflicts.any { it.resolutionStatus == ResolutionStatus.UNRESOLVED }

            itemDiffs.add(
                BriefingItemReconciliationDiff(
                    intelligenceId = item.intelligenceId,
                    isSuperseded = currentState.effectiveEntryId != item.intelligenceId && currentTimelineEntries.any { it.id == currentState.effectiveEntryId && it.ingestionTime.isAfter(historicalPkg.assembledAt) },
                    stateChanged = stateChanged,
                    significanceChanged = significanceChanged,
                    priorityChanged = priorityChanged,
                    evidenceChanged = evidenceChanged,
                    conflictChanged = conflictChanged,
                    previousStateEntryId = historicalPkg.currentState.effectiveEntryId,
                    currentStateEntryId = currentState.effectiveEntryId,
                    previousSignificance = historicalPkg.significanceLevel ?: SignificanceOutcome.SIGNIFICANT,
                    currentSignificance = currentSignal?.significanceLevel,
                    previousPriority = historicalPkg.priority ?: AttentionLevel.ROUTINE,
                    currentPriority = if (currentSignal != null) mapImportanceToAttention(currentSignal.importance) else null,
                    previousEvidenceLevel = historicalPkg.evidenceSynthesis.level,
                    currentEvidenceLevel = currentSynthesis.level
                )
            )
        }

        val status = when {
            itemDiffs.all { !it.stateChanged && !it.significanceChanged && !it.evidenceChanged } -> CommunicationReconciliationStatus.UNCHANGED
            itemDiffs.any { it.isSuperseded || it.stateChanged } -> CommunicationReconciliationStatus.CHANGED
            else -> CommunicationReconciliationStatus.PARTIALLY_CHANGED
        }

        return IntelligenceBriefingReconciliationResult(
            briefingId = historicalBriefing.id,
            snapshotId = historicalBriefing.id,
            status = status,
            itemDiffs = itemDiffs,
            reconciledAt = now
        )
    }

    private fun mapImportanceToAttention(importance: SignalImportance): AttentionLevel {
        return when (importance) {
            SignalImportance.CRITICAL -> AttentionLevel.IMMEDIATE
            SignalImportance.HIGH -> AttentionLevel.IMPORTANT
            SignalImportance.MODERATE -> AttentionLevel.ROUTINE
            SignalImportance.LOW -> AttentionLevel.ARCHIVE
        }
    }
}
