package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import java.util.*
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceBriefingAssemblyEngine] that assembles 
 * structured briefing data from authorized and cycle-selected intelligence handoffs.
 */
class DefaultIntelligenceBriefingAssemblyEngine @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val explanationEngine: IntelligenceBriefingExplanationEngine,
    private val synthesisEngine: IntelligenceEvidenceSynthesisEngine,
    private val gapEngine: IntelligenceEvidenceGapEngine,
    private val readinessEngine: IntelligenceCommunicationReadinessEngine,
    private val provenanceEngine: IntelligenceProvenanceEngine,
    private val narrativeEngine: IntelligenceNarrativeContinuityEngine,
    private val stateReconstructor: IntelligenceStateReconstructionEngine,
    private val freshnessEngine: IntelligenceFreshnessEngine,
    private val relevanceEngine: IntelligenceRelevanceEngine,
    private val continuityEngine: IntelligenceContinuityEngine,
    private val communicationAssemblyEngine: IntelligenceCommunicationAssemblyEngine,
    private val selectionEngine: IntelligenceBriefingSelectionEngine,
    private val contextEngine: IntelligenceBriefingContextEngine,
    private val uncertaintyEngine: IntelligenceUncertaintyEngine,
    private val handoffFactory: IntelligenceCommunicationHandoffFactory,
    private val orderingContract: IntelligenceCommunicationOrderingContract,
    private val selectionGate: IntelligenceCommunicationSelectionGate,
    private val reentryHandoffBoundary: IntelligenceReentryHandoffBoundary,
    private val acknowledgementBoundary: IntelligenceReentryAcknowledgementBoundary,
    private val reconciliationConsumptionBoundary: IntelligenceReentryReconciliationConsumptionBoundary
) : IntelligenceBriefingAssemblyEngine {

    override suspend fun assembleBriefing(
        prioritizedSignals: List<PrioritizedSignal>,
        atTime: Instant
    ): IntelligenceBriefing {
        // 1. Generate candidate communication packages
        val candidates = prioritizedSignals.map { prioritized ->
            val signal = prioritized.signal
            val thread = memory.findThreadByTopic(signal.title) ?: 
                IntelligenceThread(UUID.randomUUID().toString(), signal.title, signal.detectedAt, signal.detectedAt)
            
            // Context Retrieval
            val threadSignals = thread.signalIds.mapNotNull { memory.getSignalById(it) }
            val conflicts = memory.getConflictsForTopic(thread.topicIdentifier)
            val timelineEntries = memory.getTimelineEntriesForThread(thread.id)
            val timeline = IntelligenceEventTimeline(thread.id, thread.topicIdentifier, timelineEntries)
            
            val synthesis = synthesisEngine.synthesizeEvidence(thread.id, threadSignals, conflicts)
            val gap = gapEngine.analyzeGaps(thread, synthesis, conflicts)
            val readiness = readinessEngine.evaluateReadiness(signal, synthesis, gap, conflicts)
            val provenance = provenanceEngine.getSignalProvenance(signal.id)
            val narrative = narrativeEngine.reconstructNarrative(thread)
            val currentState = stateReconstructor.reconstructCurrentState(timeline)
            
            val primaryTimelineEntry = timelineEntries.find { it.signalId == signal.id }
            val freshness = primaryTimelineEntry?.let { freshnessEngine.evaluateFreshness(it, timeline, thread) }
            
            val continuity = primaryTimelineEntry?.let { entry ->
                val prevTimeline = IntelligenceEventTimeline(thread.id, thread.topicIdentifier, timelineEntries.filter { it.ingestionTime.isBefore(entry.ingestionTime) })
                val prevState = if (prevTimeline.entries.isNotEmpty()) stateReconstructor.reconstructCurrentState(prevTimeline) else null
                val relations = memory.getSupersessionRelationsForEntry(entry.id)
                continuityEngine.evaluateContinuity(entry, signal, prevState, thread, relations)
            }
            
            val relevance = relevanceEngine.evaluateRelevance(
                SourceMaterial(
                    sourceId = signal.source.name, 
                    contentId = signal.id, 
                    title = signal.title, 
                    content = signal.summary, 
                    publishedAt = signal.publishedAt, 
                    contentHash = "derived-hash"
                ),
                listOf(thread)
            )

            val pkg = communicationAssemblyEngine.assemblePackage(
                signal = signal,
                thread = thread,
                currentState = currentState,
                continuity = continuity,
                narrative = narrative,
                synthesis = synthesis,
                gap = gap,
                readiness = readiness,
                provenance = provenance,
                conflicts = conflicts,
                prioritized = prioritized,
                relevance = relevance,
                freshness = freshness
            )
            
            prioritized to pkg
        }

        // 2. Selection (Step 63)
        val selection = selectionEngine.selectBriefing(candidates)

        if (selection.orderedSignalIds.isEmpty()) {
            return IntelligenceBriefing(
                id = selection.id,
                cycleId = "cycle-${atTime.toEpochMilli()}",
                generatedAt = atTime,
                status = BriefingStatus.NO_MAJOR_CHANGES,
                items = emptyList(),
                signals = emptyMap()
            )
        }

        // 3. Boundary Gate & Handoff
        val authorizedHandoffsList = mutableListOf<IntelligenceCommunicationHandoff>()
        selection.orderedSignalIds.forEach { signalId ->
            val pkg = selection.selectedPackages[signalId]!!
            
            // Re-entry Identity: signalId::stateId
            val reentryIdentity = "${signalId}::${pkg.currentState.effectiveEntryId}"
            
            // Step 89: Perform Controlled Handoff
            val reentryResult = reentryHandoffBoundary.performHandoff(
                reentryIdentity, 
                IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY
            )

            val handoffResult = handoffFactory.createHandoff(pkg)
            if (handoffResult is HandoffResult.Success) {
                authorizedHandoffsList.add(
                    handoffResult.handoff.copy(
                        reentryHandoff = if (reentryResult.status == IntelligenceReentryHandoffStatus.HANDOFF_ACCEPTED) reentryResult else null
                    )
                )
            }
        }

        // 4. Deterministic Ordering (Step 72)
        val orderedHandoffs = authorizedHandoffsList.sortedWith(orderingContract.handoffComparator)

        // 5. Cycle Selection Gate (Step 73)
        val cycleSelectionResult = selectionGate.selectEligible(orderedHandoffs)
        val finalHandoffs = cycleSelectionResult.selectedHandoffs

        // 6. Communication Cycle Construction (Step 74)
        val cycleId = "cycle-${atTime.toEpochMilli()}"
        val cycleStatus = if (finalHandoffs.any { it.communicationPackage.priority == AttentionLevel.IMMEDIATE }) {
            BriefingStatus.ATTENTION_REQUIRED
        } else if (finalHandoffs.isEmpty()) {
            BriefingStatus.NO_MAJOR_CHANGES
        } else {
            BriefingStatus.READY
        }

        val cycle = IntelligenceCommunicationCycle(
            id = cycleId,
            timestamp = atTime,
            selectedHandoffs = finalHandoffs,
            status = cycleStatus
        )

        // 7. Final Briefing Assembly
        val briefingItems = cycle.selectedHandoffs.mapIndexed { index, handoff ->
            val pkg = handoff.communicationPackage
            val signal = pkg.provenance.signal
            
            // Step 91: Acknowledge consumption if re-entry context exists
            handoff.reentryHandoff?.let { result ->
                if (result.status == IntelligenceReentryHandoffStatus.HANDOFF_ACCEPTED) {
                    acknowledgementBoundary.acknowledge(
                        IntelligenceReentryConsumerAcknowledgement(
                            reentryIdentity = result.reentryIdentity,
                            consumer = IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY,
                            status = ReentryAcknowledgementStatus.ACCEPTED,
                            timestamp = Instant.now(),
                            reason = "Item successfully included in briefing cycle ${cycle.id}"
                        )
                    )
                }
            }
            
            // Step 106: Consume Reconciled State
            val reentryIdentity = "${signal.id}::${pkg.currentState.effectiveEntryId}"
            val reconciliationContract = reconciliationConsumptionBoundary.getReconciliationContract(reentryIdentity)

            val baseItem = IntelligenceBriefingItem(
                id = "item-${signal.id}",
                position = index,
                communicationPackageId = signal.id,
                intelligenceId = signal.id,
                threadId = handoff.threadId,
                
                currentStateReference = pkg.currentState.effectiveEntryId,
                changeReference = pkg.changeId,
                continuityReference = pkg.continuity?.level,
                significanceReference = signal.significanceLevel ?: SignificanceOutcome.SIGNIFICANT,
                priorityReference = pkg.priority ?: AttentionLevel.ROUTINE,
                evidenceReference = pkg.evidenceSynthesis.level,
                uncertaintyReference = signal.confidence,
                conflictReference = pkg.conflicts.any { it.resolutionStatus == ResolutionStatus.UNRESOLVED },
                narrativeReference = handoff.threadId,
                provenanceReference = signal.id,
                
                context = contextEngine.determineContext(pkg),
                uncertaintyModel = uncertaintyEngine.determineUncertainty(pkg),
                reentryContract = handoff.reentryHandoff?.contract,
                reconciliationContract = reconciliationContract,
                
                inclusionReason = "Authorized in cycle: ${cycle.id}",
                explanation = null,
                readiness = pkg.communicationReadiness.level
            )

            baseItem.copy(
                explanation = explanationEngine.explain(baseItem, signal)
            )
        }

        return IntelligenceBriefing(
            id = "briefing-${cycle.id}",
            cycleId = cycle.id,
            generatedAt = atTime,
            status = cycle.status,
            items = briefingItems,
            signals = finalHandoffs.associate { it.intelligenceId to it.communicationPackage.provenance.signal },
            handoffs = finalHandoffs.associateBy { it.intelligenceId }
        )
    }
}
