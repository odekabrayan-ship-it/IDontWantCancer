package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryAcknowledgementBoundary
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command result acknowledgement boundary.
 * Delegates to Step 91 for re-entry results and provides deterministic 
 * status recording.
 */
class DefaultIntelligenceCommandResultAcknowledgementBoundary @Inject constructor(
    private val reentryAcknowledgementBoundary: IntelligenceReentryAcknowledgementBoundary
) : IntelligenceCommandResultAcknowledgementBoundary {

    override suspend fun acknowledge(
        request: IntelligenceCommandAcknowledgementRequest
    ): IntelligenceCommandAcknowledgementResult {
        val result = request.result
        val now = Instant.now()
        val operationId = result.operationId ?: return anonymousAcknowledgement(now)

        // 1. Identify Re-entry Acknowledgement (Step 91 Integration)
        if (operationId.contains("::")) {
            return handleReentryAcknowledgement(operationId, now)
        }

        // 2. Default Policy: Recognize successful receipt of result
        return IntelligenceCommandAcknowledgementResult(
            operationId = operationId,
            status = CommandAcknowledgementStatus.ACKNOWLEDGED,
            reason = "Result delivered and recorded by presentation coordinator.",
            acknowledgedAt = now
        )
    }

    private suspend fun handleReentryAcknowledgement(
        identity: String,
        now: Instant
    ): IntelligenceCommandAcknowledgementResult {
        // Step 91: Formalize receipt in domain ledger
        val domainAck = IntelligenceReentryConsumerAcknowledgement(
            reentryIdentity = identity,
            consumer = IntelligenceConsumerIdentity.INTELLIGENCE_SYNC, // Mapping to existing consumer
            status = ReentryAcknowledgementStatus.ACCEPTED,
            timestamp = now,
            reason = "Acknowledgement recorded via Command Result Boundary (Step 124)."
        )

        reentryAcknowledgementBoundary.acknowledge(domainAck)

        return IntelligenceCommandAcknowledgementResult(
            operationId = identity,
            status = CommandAcknowledgementStatus.ACKNOWLEDGED,
            reason = "Step 91 recorded receipt for re-entry identity $identity.",
            acknowledgedAt = now
        )
    }

    private fun anonymousAcknowledgement(now: Instant) = IntelligenceCommandAcknowledgementResult(
        operationId = "anonymous",
        status = CommandAcknowledgementStatus.ACKNOWLEDGED,
        reason = "Anonymous result receipt recognized.",
        acknowledgedAt = now
    )
}
