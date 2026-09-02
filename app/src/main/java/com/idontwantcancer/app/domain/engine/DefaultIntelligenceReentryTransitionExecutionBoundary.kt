package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the transition execution boundary.
 * Delegates the authoritative write to the lifecycle ledger (Step 79/81 mechanism).
 */
class DefaultIntelligenceReentryTransitionExecutionBoundary @Inject constructor(
    private val memory: IntelligenceMemoryRepository
) : IntelligenceReentryTransitionExecutionBoundary {

    override suspend fun executeTransition(
        authorization: IntelligenceReentryTransitionAuthorization
    ): ReentryTransitionResult {
        // Authoritative mutation path (Step 81 atomicity preserved)
        val updated = authorization.updatedLifecycle
        val previousState = authorization.previousState
        val reason = authorization.reason
        val now = Instant.now()

        val history = memory.getReentryAuditHistory(updated.reentryIdentity)
        val nextSequence = history.size

        val auditEntry = IntelligenceReentryAuditEntry(
            reentryIdentity = updated.reentryIdentity,
            sequenceNumber = nextSequence,
            previousState = previousState,
            newState = updated.currentState,
            transitionReason = reason,
            timestamp = now
        )

        memory.recordReentryTransition(updated, auditEntry)
        
        return ReentryTransitionResult.Accepted(updated)
    }
}
