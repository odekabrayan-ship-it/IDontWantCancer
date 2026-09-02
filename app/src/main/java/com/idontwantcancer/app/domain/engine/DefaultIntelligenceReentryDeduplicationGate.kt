package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceReentryDeduplicationGate] that uses 
 * stable domain identities and state pointers to prevent duplicate admission.
 */
class DefaultIntelligenceReentryDeduplicationGate @Inject constructor(
    private val memory: IntelligenceMemoryRepository
) : IntelligenceReentryDeduplicationGate {

    override suspend fun checkDeduplication(
        reentryResult: IntelligenceReentryResult,
        diff: BriefingItemReconciliationDiff
    ): IntelligenceReentryDeduplicationResult {
        val now = Instant.now()
        val signal = memory.getSignalById(reentryResult.intelligenceId)
        
        val targetStateId = diff.currentStateEntryId
        val identity = "${reentryResult.intelligenceId}::$targetStateId"
        
        val isAlreadyAdmitted = signal?.lastAdmittedStateEntryId == targetStateId

        val status = if (isAlreadyAdmitted) {
            DeduplicationStatus.ALREADY_ADMITTED
        } else {
            DeduplicationStatus.NEW_REENTRY
        }

        return IntelligenceReentryDeduplicationResult(
            reentryIdentity = identity,
            status = status,
            intelligenceId = reentryResult.intelligenceId,
            stateEntryId = targetStateId,
            reason = if (isAlreadyAdmitted) "This specific state change has already been admitted for evaluation."
                     else "Genuinely new re-entry event detected.",
            evaluatedAt = now
        )
    }
}
