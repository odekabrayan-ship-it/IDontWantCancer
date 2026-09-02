package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceReentryLifecycleLedger] that persists 
 * re-entry lifecycle state in the agency's memory and enforces transition rules.
 */
class DefaultIntelligenceReentryLifecycleLedger @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val transitionGuard: IntelligenceReentryTransitionGuard
) : IntelligenceReentryLifecycleLedger {

    override suspend fun admitEvent(
        deduplicationResult: IntelligenceReentryDeduplicationResult
    ): IntelligenceReentryLifecycle {
        val now = Instant.now()
        val lifecycle = IntelligenceReentryLifecycle(
            reentryIdentity = deduplicationResult.reentryIdentity,
            currentState = ReentryLifecycleState.ADMITTED,
            intelligenceId = deduplicationResult.intelligenceId,
            stateEntryId = deduplicationResult.stateEntryId,
            admittedAt = now,
            lastTransitionAt = now,
            transitionReason = "Admitted from deduplication gate."
        )

        val auditEntry = IntelligenceReentryAuditEntry(
            reentryIdentity = lifecycle.reentryIdentity,
            sequenceNumber = 0,
            previousState = null,
            newState = ReentryLifecycleState.ADMITTED,
            transitionReason = lifecycle.transitionReason,
            timestamp = now
        )

        memory.recordReentryTransition(lifecycle, auditEntry)
        return lifecycle
    }

    override suspend fun transitionState(
        identity: String,
        newState: ReentryLifecycleState,
        reason: String?
    ): ReentryTransitionResult {
        val current = memory.getReentryLifecycleByIdentity(identity) 
            ?: return ReentryTransitionResult.Rejected(
                reason = ReentryTransitionRejectionReason.EVENT_NOT_FOUND,
                currentState = ReentryLifecycleState.ADMITTED, // Placeholder
                requestedState = newState
            )

        // Delegate to transition guard
        val validation = transitionGuard.validateTransition(current, newState)

        if (validation is ReentryTransitionResult.Accepted) {
            return recordTransition(validation.updatedLifecycle, current.currentState, reason)
        }

        return validation
    }

    override suspend fun transitionByDecision(
        record: IntelligenceReentryDecisionRecord
    ): ReentryTransitionResult {
        val identity = record.reentryIdentity
        val current = memory.getReentryLifecycleByIdentity(identity) 
            ?: return ReentryTransitionResult.Rejected(
                reason = ReentryTransitionRejectionReason.EVENT_NOT_FOUND,
                currentState = ReentryLifecycleState.ADMITTED, // Placeholder
                requestedState = record.proposedState ?: ReentryLifecycleState.ADMITTED
            )

        // Delegate to decision validation in the guard (Step 80 authority)
        val validation = transitionGuard.validateDecision(current, record)

        if (validation is ReentryTransitionResult.Accepted) {
            val reason = "Transition authorized by decision record: ${record.status}"
            return recordTransition(validation.updatedLifecycle, current.currentState, reason)
        }

        return validation
    }

    private suspend fun recordTransition(
        updated: IntelligenceReentryLifecycle,
        previousState: ReentryLifecycleState,
        reason: String?
    ): ReentryTransitionResult {
        val now = Instant.now()
        val history = memory.getReentryAuditHistory(updated.reentryIdentity)
        val nextSequence = history.size

        val lifecycleWithReason = updated.copy(transitionReason = reason)
        
        val auditEntry = IntelligenceReentryAuditEntry(
            reentryIdentity = updated.reentryIdentity,
            sequenceNumber = nextSequence,
            previousState = previousState,
            newState = updated.currentState,
            transitionReason = reason,
            timestamp = now
        )

        memory.recordReentryTransition(lifecycleWithReason, auditEntry)
        return ReentryTransitionResult.Accepted(lifecycleWithReason)
    }
}
