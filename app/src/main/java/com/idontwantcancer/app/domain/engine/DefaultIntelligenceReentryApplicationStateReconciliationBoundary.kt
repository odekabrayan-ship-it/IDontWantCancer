package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.BriefingRepository
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the application-state reconciliation boundary.
 * Compares completion facts with persistent signals and briefing data.
 */
class DefaultIntelligenceReentryApplicationStateReconciliationBoundary @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val briefingRepository: BriefingRepository
) : IntelligenceReentryApplicationStateReconciliationBoundary {

    override suspend fun reconcileWithApplication(
        completion: IntelligenceReentryTransitionCompletionResult
    ): IntelligenceReentryApplicationStateReconciliationResult {
        val now = Instant.now()
        val identity = completion.reentryIdentity
        val signalId = completion.verificationResult.reentryIdentity.split("::").firstOrNull() ?: ""

        // 1. Authoritative Signal Check
        // If re-entry is COMPLETED, the Signal's lastAdmittedStateEntryId must match.
        val signal = memory.getSignalById(signalId)
        val expectedEntryId = completion.verificationResult.expectedState?.let { 
            // In our identity model (signalId::stateId), the stateId is the target
            completion.reentryIdentity.split("::").getOrNull(1)
        }

        val isSignalConsistent = signal?.lastAdmittedStateEntryId == expectedEntryId

        // 2. Briefing Check
        // If re-entry is COMPLETED, we check if the current briefing reflects it.
        // This is a passive check; we don't force a refresh.
        val briefing = briefingRepository.getCurrentBriefing()
        val briefingItem = briefing.items.find { it.intelligenceId == signalId }
        
        val isBriefingConsistent = if (completion.status == ReentryTransitionCompletionStatus.COMPLETED) {
            // A completed re-entry should have a contract in the current briefing item
            briefingItem?.reentryContract?.reentryIdentity == identity
        } else {
            // If NOT_COMPLETED, it shouldn't have a verified contract matching this identity
            briefingItem?.reentryContract?.reentryIdentity != identity
        }

        val status = if (isSignalConsistent && isBriefingConsistent) {
            ApplicationStateReconciliationStatus.CONSISTENT
        } else {
            ApplicationStateReconciliationStatus.INCONSISTENT
        }

        return IntelligenceReentryApplicationStateReconciliationResult(
            reentryIdentity = identity,
            status = status,
            details = when {
                !isSignalConsistent -> "Signal lastAdmittedStateEntryId mismatch: expected $expectedEntryId, found ${signal?.lastAdmittedStateEntryId}"
                !isBriefingConsistent -> "Briefing item inconsistency for identity $identity"
                else -> null
            },
            verifiedAt = now
        )
    }
}
