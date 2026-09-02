package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.data.datasource.IntelligenceDataSource
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.BriefingRepository
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import com.idontwantcancer.app.domain.repository.IntelligenceSourceRegistry
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceCycleCoordinator] that coordinates
 * the various components of the intelligence pipeline.
 */
class DefaultIntelligenceCycleCoordinator @Inject constructor(
    private val sourceRegistry: IntelligenceSourceRegistry,
    private val intelligenceDataSource: IntelligenceDataSource,
    private val memory: IntelligenceMemoryRepository,
    private val briefingRepository: BriefingRepository,
    private val deduplicationEngine: IntelligenceDeduplicationEngine,
    private val relevanceEngine: IntelligenceRelevanceEngine,
    private val changeDiffEngine: ChangeDiffEngine,
    private val significanceGate: IntelligenceSignificanceGate,
    private val confidenceGate: EvidenceConfidenceGate,
    private val contradictionEngine: IntelligenceContradictionResolutionEngine,
    private val stateReconstructor: IntelligenceStateReconstructionEngine,
    private val supersessionEngine: IntelligenceSupersessionEngine,
    private val stateTransitionEngine: IntelligenceStateTransitionEngine,
    private val continuityEngine: IntelligenceContinuityEngine,
    private val reconciliationEngine: IntelligenceStateReconciliationEngine,
    private val evidenceSynthesisEngine: IntelligenceEvidenceSynthesisEngine,
    private val evidenceGapEngine: IntelligenceEvidenceGapEngine,
    private val integrityEngine: IntelligenceLifecycleIntegrityEngine,
    private val evidenceAssessor: EvidenceAssessmentEngine,
    private val signalFormer: SignalFormationEngine,
    private val prioritizer: IntelligencePrioritizationEngine,
    private val briefingAssemblyEngine: IntelligenceBriefingAssemblyEngine
) : IntelligenceCycleCoordinator {

    override suspend fun runCycle(): IntelligenceCycleResult {
        val startTime = Instant.now()
        val failures = mutableListOf<CycleFailure>()
        
        var sourceItemsCount = 0
        var changesCount = 0
        var significantChangesCount = 0
        var signalsCount = 0

        val sources = sourceRegistry.getEnabledSources()

        for (source in sources) {
            try {
                val newMaterials = intelligenceDataSource.fetchSourceMaterial(source.id)
                sourceItemsCount += newMaterials.size

                val existingEvents = memory.getAllConsolidatedEvents()

                for (material in newMaterials) {
                    // 1. Deduplication & Consolidation
                    val dedupResult = deduplicationEngine.checkDeduplication(material, existingEvents)
                    
                    val consolidatedEvent = when (dedupResult.type) {
                        DeduplicationType.DUPLICATE_EVENT -> {
                            val existing = memory.getConsolidatedEventById(dedupResult.existingEventId!!)
                            deduplicationEngine.consolidate(material, existing)
                        }
                        DeduplicationType.UNIQUE_EVENT -> {
                            deduplicationEngine.consolidate(material, null)
                        }
                        DeduplicationType.POSSIBLE_DUPLICATE -> {
                            // Conservative: treat as unique for now
                            deduplicationEngine.consolidate(material, null)
                        }
                    }
                    
                    memory.saveConsolidatedEvent(consolidatedEvent)

                    // 2. Relevance & Thread Association
                    val existingThreadsForTopic = listOfNotNull(memory.findThreadByTopic(consolidatedEvent.topicIdentifier))
                    val relevanceResult = relevanceEngine.evaluateRelevance(material, existingThreadsForTopic)

                    var relatedThread: IntelligenceThread? = null
                    if (relevanceResult.level == RelevanceLevel.RELEVANT) {
                        relatedThread = memory.findThreadByTopic(consolidatedEvent.topicIdentifier)
                    }

                    if (relatedThread == null) {
                        relatedThread = IntelligenceThread(
                            id = UUID.randomUUID().toString(),
                            topicIdentifier = consolidatedEvent.topicIdentifier,
                            firstDetectedAt = startTime,
                            lastUpdatedAt = startTime
                        )
                        memory.saveThread(relatedThread)
                    }

                    val lastSignalId = relatedThread.signalIds.lastOrNull()
                    val lastSignal = lastSignalId?.let { memory.getSignalById(it) }

                    // 3. Detect Changes (Intelligence Diff)
                    val previousMaterial = memory.getPreviousMaterial(material.contentId)
                    val detectedChanges = changeDiffEngine.detectChanges(
                        current = material,
                        previousMaterial = previousMaterial,
                        relatedThread = relatedThread,
                        lastSignal = lastSignal
                    )
                    changesCount += detectedChanges.size

                    for (change in detectedChanges) {
                        // 4. Assess Evidence
                        val assessment = evidenceAssessor.assess(change, source)
                        
                        // 5. Significance Gate
                        val significanceDecision = significanceGate.evaluate(change, assessment, source)
                        
                        if (significanceDecision.outcome != SignificanceOutcome.NOT_SIGNIFICANT) {
                            significantChangesCount++

                            // 6. Supersession Detection
                            val existingTimelineEntries = memory.getTimelineEntriesForThread(relatedThread.id)
                            val timelineEntryId = UUID.randomUUID().toString()
                            val timelineType = mapChangeToTimelineType(change.type)
                            val newEntryCandidate = TimelineEntry(
                                id = timelineEntryId,
                                threadId = relatedThread.id,
                                type = timelineType,
                                description = material.title,
                                eventTime = material.publishedAt,
                                publicationTime = material.publishedAt,
                                ingestionTime = startTime,
                                sourceMaterialId = material.contentId,
                                changeId = change.id
                            )
                            val relations = supersessionEngine.detectSupersession(newEntryCandidate, existingTimelineEntries)

                            // 7. Contradiction Analysis
                            val allSignalsForConflict = memory.getAllSignals()
                            val conflicts = contradictionEngine.analyzeContradictions(assessment, allSignalsForConflict, relations)

                            // 8. Evidence Confidence Gate
                            val confidenceDecision = confidenceGate.evaluate(assessment, source, conflicts)

                            // 9. Signal Consolidation / Formation
                            if (lastSignal != null && dedupResult.type == DeduplicationType.DUPLICATE_EVENT) {
                                val updatedSignal = lastSignal.copy(
                                    supportingSources = (lastSignal.supportingSources + SignalSource(source.name, material.canonicalUrl)).distinct()
                                )
                                memory.saveSignal(updatedSignal)
                            } else {
                                // 10. Form New Signal
                                val formationResult = signalFormer.formSignal(
                                    significance = significanceDecision,
                                    confidence = confidenceDecision,
                                    conflicts = conflicts,
                                    source = source,
                                    material = material
                                )
                                
                                if (formationResult is SignalFormationResult.SignalCreated) {
                                    val signal = formationResult.signal
                                    memory.saveSignal(signal)
                                    signalsCount++

                                    val newEntry = newEntryCandidate.copy(signalId = signal.id)
                                    memory.saveTimelineEntry(newEntry)

                                    relations.forEach { memory.saveSupersessionRelation(it) }

                                    // 11. Evidence Synthesis
                                    val threadSignals = (relatedThread.signalIds + signal.id)
                                        .mapNotNull { memory.getSignalById(it) }
                                    
                                    val synthesis = evidenceSynthesisEngine.synthesizeEvidence(
                                        threadId = relatedThread.id,
                                        signals = threadSignals,
                                        conflicts = conflicts
                                    )

                                    // 12. Evidence Gap Analysis
                                    evidenceGapEngine.analyzeGaps(
                                        thread = relatedThread,
                                        synthesis = synthesis,
                                        conflicts = conflicts
                                    )

                                    // 13. State Transition Evaluation
                                    // True previous state (before this signal/entry)
                                    val previousTimeline = IntelligenceEventTimeline(
                                        threadId = relatedThread.id,
                                        topicIdentifier = relatedThread.topicIdentifier,
                                        entries = existingTimelineEntries
                                    )
                                    val previousState = if (existingTimelineEntries.isEmpty()) null 
                                                        else stateReconstructor.reconstructCurrentState(previousTimeline)
                                    
                                    val transition = stateTransitionEngine.evaluateTransition(
                                        previousState = previousState,
                                        newEntry = newEntry,
                                        relations = relations,
                                        conflicts = conflicts,
                                        synthesis = synthesis
                                    )
                                    
                                    transition?.let { memory.saveStateTransition(it) }

                                    // 14. Continuity Evaluation
                                    val continuityResult = continuityEngine.evaluateContinuity(
                                        newEntry = newEntry,
                                        signal = signal,
                                        previousState = previousState,
                                        thread = relatedThread,
                                        relations = relations
                                    )

                                    // 15. State Reconciliation
                                    val updatedEntries = memory.getTimelineEntriesForThread(relatedThread.id)
                                    val currentTimeline = IntelligenceEventTimeline(
                                        threadId = relatedThread.id,
                                        topicIdentifier = relatedThread.topicIdentifier,
                                        entries = updatedEntries
                                    )
                                    val candidateState = stateReconstructor.reconstructCurrentState(currentTimeline)

                                    val reconciliationResult = reconciliationEngine.reconcileState(
                                        thread = relatedThread,
                                        previousState = previousState,
                                        newState = candidateState,
                                        continuity = continuityResult,
                                        transition = transition
                                    )

                                    // 16. Update Thread with authoritative status
                                    memory.saveThread(
                                        relatedThread.copy(
                                            signalIds = (relatedThread.signalIds + signal.id).distinct(),
                                            lastUpdatedAt = startTime,
                                            currentStatus = candidateState.summary
                                        )
                                    )
                                }
                            }
                        }
                    }
                    
                    memory.saveSourceMaterial(material)
                }
            } catch (e: Exception) {
                failures.add(CycleFailure(source.id, "Pipeline", e.message ?: "Unknown error"))
            }
        }

        // 17. Global Integrity Check
        val integrityResult = integrityEngine.verifyGlobalIntegrity()

        // 18. Prioritization and Briefing Assembly
        val briefing = try {
            val allSignals = memory.getAllSignals()
            val prioritized = prioritizer.prioritize(allSignals)
            val assembled = briefingAssemblyEngine.assembleBriefing(prioritized, startTime)
            
            // Step 75: Save Historical Snapshot
            briefingRepository.saveBriefingSnapshot(assembled)
            
            assembled
        } catch (e: Exception) {
            failures.add(CycleFailure(null, "Briefing", e.message ?: "Briefing failed"))
            null
        }

        return IntelligenceCycleResult(
            cycleTimestamp = startTime,
            sourcesCheckedCount = sources.size,
            newSourceItemsCount = sourceItemsCount,
            detectedChangesCount = changesCount,
            significantChangesCount = significantChangesCount,
            signalsFormedCount = signalsCount,
            briefing = briefing,
            integrityResult = integrityResult,
            failures = failures
        )
    }

    private fun mapChangeToTimelineType(changeType: ChangeType): TimelineEntryType {
        return when (changeType) {
            ChangeType.CORRECTION -> TimelineEntryType.CORRECTION
            ChangeType.REVERSAL -> TimelineEntryType.REVERSAL
            ChangeType.RECOMMENDATION_CHANGE -> TimelineEntryType.RECOMMENDATION_CHANGE
            ChangeType.SAFETY_ACTION -> TimelineEntryType.REGULATORY_ACTION
            else -> TimelineEntryType.MATERIAL_CHANGE
        }
    }
}
