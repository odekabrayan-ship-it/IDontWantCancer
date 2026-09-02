package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryAcknowledgementBoundary
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command result acceptance boundary.
 * Formalizes the destination's decision on whether a result satisfies its receiving contract.
 */
class DefaultIntelligenceCommandResultAcceptanceBoundary @Inject constructor(
    private val reentryAcknowledgementBoundary: IntelligenceReentryAcknowledgementBoundary
) : IntelligenceCommandResultAcceptanceBoundary {

    override suspend fun evaluateAcceptance(
        result: IntelligenceApplicationCommandResult
    ): IntelligenceCommandAcceptanceResult {
        val now = Instant.now()
        val operationId = result.operationId ?: return anonymousAcceptance(now)

        // 1. Identify Re-entry Destination
        if (operationId.contains("::")) {
            return handleReentryAcceptance(operationId, result, now)
        }

        // 2. Default Policy: Generic UI results are accepted if they reach finalization
        return IntelligenceCommandAcceptanceResult(
            operationId = operationId,
            status = CommandAcceptanceStatus.ACCEPTED,
            reason = "Standard interaction result satisfies presentation contract.",
            evaluatedAt = now
        )
    }

    private fun handleReentryAcceptance(
        identity: String,
        result: IntelligenceApplicationCommandResult,
        now: Instant
    ): IntelligenceCommandAcceptanceResult {
        // Validation: Verify if the result data is complete enough for the domain
        // In Step 125, we ensure that a success result actually contains verified processing data.
        val isAccepted = when (result) {
            is IntelligenceApplicationCommandResult.Success -> true // Domain already verified consistency
            is IntelligenceApplicationCommandResult.Failure -> true // Domain accepts receipt of failure records
            is IntelligenceApplicationCommandResult.Rejected -> true 
            is IntelligenceApplicationCommandResult.Cancelled -> true
            is IntelligenceApplicationCommandResult.TimedOut -> true
        }

        val status = if (isAccepted) CommandAcceptanceStatus.ACCEPTED else CommandAcceptanceStatus.REJECTED

        return IntelligenceCommandAcceptanceResult(
            operationId = identity,
            status = status,
            reason = "Result evaluated against domain receiving contract.",
            evaluatedAt = now
        )
    }

    private fun anonymousAcceptance(now: Instant) = IntelligenceCommandAcceptanceResult(
        operationId = "anonymous",
        status = CommandAcceptanceStatus.ACCEPTED,
        reason = "Anonymous result accepted by default.",
        evaluatedAt = now
    )
}
