package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceReentryIntegrityGate] that replays
 * audit history to verify persisted lifecycle state.
 */
class DefaultIntelligenceReentryIntegrityGate @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val transitionGuard: IntelligenceReentryTransitionGuard
) : IntelligenceReentryIntegrityGate {

    override suspend fun verifyIntegrity(reentryIdentity: String): IntelligenceReentryIntegrityResult {
        val now = Instant.now()
        val storedLifecycle = memory.getReentryLifecycleByIdentity(reentryIdentity)
        val auditHistory = memory.getReentryAuditHistory(reentryIdentity).sortedBy { it.sequenceNumber }

        val failureReasons = mutableListOf<ReentryIntegrityFailureReason>()

        // 1. Check for basic existence
        if (storedLifecycle == null && auditHistory.isEmpty()) {
            return IntelligenceReentryIntegrityResult(
                reentryIdentity = reentryIdentity,
                status = ReentryIntegrityStatus.INTEGRITY_FAILED,
                storedState = null,
                reconstructedState = null,
                failureReasons = listOf(ReentryIntegrityFailureReason.MISSING_HISTORY),
                verifiedAt = now
            )
        }

        // 2. Validate identity across history
        if (auditHistory.any { it.reentryIdentity != reentryIdentity }) {
            failureReasons.add(ReentryIntegrityFailureReason.IDENTITY_MISMATCH)
        }

        // 3. Replay History
        var reconstructedState: ReentryLifecycleState? = null
        var lastSequence = -1

        for (entry in auditHistory) {
            // Sequence Check
            if (entry.sequenceNumber != lastSequence + 1) {
                failureReasons.add(ReentryIntegrityFailureReason.AMBIGUOUS_ORDER)
            }
            lastSequence = entry.sequenceNumber

            // Transition Logic Check
            if (reconstructedState == null) {
                // First entry must be Admission
                if (entry.newState != ReentryLifecycleState.ADMITTED || entry.previousState != null) {
                    failureReasons.add(ReentryIntegrityFailureReason.INVALID_TRANSITION_HISTORY)
                }
            } else {
                // Consistency between audit records
                if (entry.previousState != reconstructedState) {
                    failureReasons.add(ReentryIntegrityFailureReason.INVALID_TRANSITION_HISTORY)
                }

                // Verify transition legality via Guard (Logic reuse)
                val mockLifecycle = IntelligenceReentryLifecycle(
                    reentryIdentity = reentryIdentity,
                    currentState = reconstructedState,
                    intelligenceId = "", // Not needed for guard logic in Step 80
                    stateEntryId = null,
                    admittedAt = now,
                    lastTransitionAt = now
                )
                val validation = transitionGuard.validateTransition(mockLifecycle, entry.newState)
                if (validation is ReentryTransitionResult.Rejected) {
                    failureReasons.add(ReentryIntegrityFailureReason.INVALID_TRANSITION_HISTORY)
                }
            }
            reconstructedState = entry.newState
        }

        // 4. Compare with Stored State
        if (storedLifecycle != null && storedLifecycle.currentState != reconstructedState) {
            failureReasons.add(ReentryIntegrityFailureReason.STATE_MISMATCH)
        }

        val status = if (failureReasons.isEmpty()) {
            ReentryIntegrityStatus.INTEGRITY_CONFIRMED
        } else {
            ReentryIntegrityStatus.INTEGRITY_FAILED
        }

        return IntelligenceReentryIntegrityResult(
            reentryIdentity = reentryIdentity,
            status = status,
            storedState = storedLifecycle?.currentState,
            reconstructedState = reconstructedState,
            failureReasons = failureReasons.distinct(),
            verifiedAt = now
        )
    }
}
