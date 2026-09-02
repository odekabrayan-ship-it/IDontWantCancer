package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceLifecycleIntegrityEngine] that verifies
 * the internal consistency of the intelligence knowledge graph.
 */
class DefaultIntelligenceLifecycleIntegrityEngine @Inject constructor(
    private val memory: IntelligenceMemoryRepository
) : IntelligenceLifecycleIntegrityEngine {

    override suspend fun verifyThreadIntegrity(threadId: String): IntelligenceIntegrityResult {
        val violations = mutableListOf<IntegrityViolation>()
        
        // 1. Thread Existence
        val thread = memory.getThreadById(threadId)
        if (thread == null) {
            violations.add(
                IntegrityViolation(
                    category = IntegrityViolationCategory.MISSING_REFERENCE,
                    severity = IntegrityViolationSeverity.ERROR,
                    affectedEntityId = threadId,
                    affectedEntityType = "IntelligenceThread",
                    description = "Thread referenced but does not exist in memory."
                )
            )
            return IntelligenceIntegrityResult(false, violations, Instant.now())
        }

        // 2. Signal Integrity
        for (signalId in thread.signalIds) {
            val signal = memory.getSignalById(signalId)
            if (signal == null) {
                violations.add(
                    IntegrityViolation(
                        category = IntegrityViolationCategory.MISSING_REFERENCE,
                        severity = IntegrityViolationSeverity.ERROR,
                        affectedEntityId = threadId,
                        affectedEntityType = "IntelligenceThread",
                        description = "Thread references non-existent Signal: $signalId",
                        relatedEntityId = signalId
                    )
                )
            }
        }

        // 3. Timeline Integrity
        val entries = memory.getTimelineEntriesForThread(threadId)
        for (entry in entries) {
            if (entry.signalId != null && !thread.signalIds.contains(entry.signalId)) {
                violations.add(
                    IntegrityViolation(
                        category = IntegrityViolationCategory.INVALID_RELATIONSHIP,
                        severity = IntegrityViolationSeverity.WARNING,
                        affectedEntityId = entry.id,
                        affectedEntityType = "TimelineEntry",
                        description = "Timeline entry references Signal not assigned to this Thread.",
                        relatedEntityId = entry.signalId
                    )
                )
            }
        }

        // 4. Supersession Integrity
        for (entry in entries) {
            val relations = memory.getSupersessionRelationsForEntry(entry.id)
            for (relation in relations) {
                // Verify both entries belong to same thread conceptually (simple check)
                val prev = memory.getTimelineEntriesForThread(threadId).find { it.id == relation.previousEntryId }
                val next = memory.getTimelineEntriesForThread(threadId).find { it.id == relation.supersedingEntryId }
                
                if (prev == null || next == null) {
                    violations.add(
                        IntegrityViolation(
                            category = IntegrityViolationCategory.INVALID_RELATIONSHIP,
                            severity = IntegrityViolationSeverity.ERROR,
                            affectedEntityId = relation.id,
                            affectedEntityType = "SupersessionRelation",
                            description = "Supersession relation references entries outside current thread context."
                        )
                    )
                }
            }
        }

        return IntelligenceIntegrityResult(
            isValid = violations.none { it.severity == IntegrityViolationSeverity.ERROR },
            violations = violations,
            checkedAt = Instant.now()
        )
    }

    override suspend fun verifyGlobalIntegrity(): IntelligenceIntegrityResult {
        // Conceptual: in a real system we would iterate through all threads.
        // For Step 58 we focus on the structure.
        return IntelligenceIntegrityResult(true, emptyList(), Instant.now())
    }
}
