package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import javax.inject.Inject

/**
 * Default implementation of the query boundary that delegates verification 
 * to the Step 83 recovery boundary.
 */
 class DefaultIntelligenceReentryLifecycleQueryBoundary @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val recoveryBoundary: IntelligenceReentryRecoveryBoundary
) : IntelligenceReentryLifecycleQueryBoundary {

    override suspend fun getVerifiedLifecycle(reentryIdentity: String): ReentryRecoveryResult {
        // Delegates directly to verified boundary (Step 83)
        return recoveryBoundary.getVerifiedReentry(reentryIdentity)
    }

    override suspend fun getVerifiedHistory(reentryIdentity: String): ReentryHistoryResult {
        // Enforce Step 83 verification before returning history (Step 84 rule)
        val recovery = recoveryBoundary.getVerifiedReentry(reentryIdentity)

        return when (recovery) {
            is ReentryRecoveryResult.Verified -> {
                val history = memory.getReentryAuditHistory(reentryIdentity)
                // Deterministic sort by sequence number
                ReentryHistoryResult.Verified(history.sortedBy { it.sequenceNumber })
            }
            is ReentryRecoveryResult.Unverified -> {
                ReentryHistoryResult.Unverified(
                    identity = reentryIdentity,
                    reason = "History query failed: ${recovery.reason}"
                )
            }
        }
    }

    override suspend fun getConsumptionContract(reentryIdentity: String): IntelligenceReentryHandoffConsumptionContract? {
        val result = recoveryBoundary.getVerifiedReentry(reentryIdentity)
        return if (result is ReentryRecoveryResult.Verified) {
            val lifecycle = result.lifecycle
            IntelligenceReentryHandoffConsumptionContract(
                reentryIdentity = lifecycle.reentryIdentity,
                verifiedState = lifecycle.currentState,
                intelligenceId = lifecycle.intelligenceId,
                stateEntryId = lifecycle.stateEntryId,
                transitionReason = lifecycle.transitionReason,
                admittedAt = lifecycle.admittedAt,
                lastTransitionAt = lifecycle.lastTransitionAt,
                isTerminal = isTerminal(lifecycle.currentState)
            )
        } else {
            null
        }
    }

    private fun isTerminal(state: ReentryLifecycleState): Boolean {
        return state == ReentryLifecycleState.COMPLETED || 
               state == ReentryLifecycleState.REJECTED || 
               state == ReentryLifecycleState.SUPERSEDED
    }
}
