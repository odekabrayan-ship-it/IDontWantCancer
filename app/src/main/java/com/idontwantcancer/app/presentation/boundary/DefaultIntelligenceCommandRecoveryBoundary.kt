package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryRecoveryBoundary
import com.idontwantcancer.app.domain.model.ReentryRecoveryResult
import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command recovery boundary.
 * Delegates recovery decisions to specialized authoritative engines (e.g. Step 82).
 */
class DefaultIntelligenceCommandRecoveryBoundary @Inject constructor(
    private val reentryRecoveryBoundary: IntelligenceReentryRecoveryBoundary
) : IntelligenceCommandRecoveryBoundary {

    override suspend fun evaluateRecovery(
        result: IntelligenceApplicationCommandResult
    ): IntelligenceCommandRecoveryResult {
        val now = Instant.now()
        val operationId = result.operationId ?: return noRecoveryRequired(now)

        // 1. Filter for Re-entry Specific Recovery (Step 82)
        // If the operationId follows the re-entry identity pattern (contains ::)
        if (operationId.contains("::")) {
            return handleReentryRecovery(operationId, now)
        }

        // 2. Default Policy: No automatic recovery for generic commands
        return noRecoveryRequired(now)
    }

    private suspend fun handleReentryRecovery(
        identity: String,
        now: Instant
    ): IntelligenceCommandRecoveryResult {
        // Delegate to Step 82 Authority
        val recoveryResult = reentryRecoveryBoundary.getVerifiedReentry(identity)

        return when (recoveryResult) {
            is ReentryRecoveryResult.Verified -> {
                IntelligenceCommandRecoveryResult(
                    status = CommandRecoveryStatus.RECOVERED,
                    reason = "Step 82 verified authoritative state; consistent state restored.",
                    evaluatedAt = now
                )
            }
            is ReentryRecoveryResult.Unverified -> {
                IntelligenceCommandRecoveryResult(
                    status = CommandRecoveryStatus.RECOVERY_FAILED,
                    reason = "Step 82 could not verify state: ${recoveryResult.reason}",
                    evaluatedAt = now
                )
            }
        }
    }

    private fun noRecoveryRequired(now: Instant) = IntelligenceCommandRecoveryResult(
        status = CommandRecoveryStatus.RECOVERY_NOT_REQUIRED,
        reason = "Command outcome does not meet existing recovery triggers.",
        evaluatedAt = now
    )
}
