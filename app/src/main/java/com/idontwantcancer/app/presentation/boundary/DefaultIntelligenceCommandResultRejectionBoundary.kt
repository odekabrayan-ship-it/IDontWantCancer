package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryAcknowledgementBoundary
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command result rejection boundary.
 * Provides deterministic recording of result refusals.
 */
class DefaultIntelligenceCommandResultRejectionBoundary @Inject constructor(
    private val reentryAcknowledgementBoundary: IntelligenceReentryAcknowledgementBoundary
) : IntelligenceCommandResultRejectionBoundary {

    override suspend fun recordRejection(
        result: IntelligenceApplicationCommandResult,
        reason: String
    ): IntelligenceCommandRejectionResult {
        val now = Instant.now()
        val operationId = result.operationId ?: "anonymous"

        // 1. Identify Re-entry Destination (Step 91 Integration)
        if (operationId.contains("::")) {
            handleReentryRejection(operationId, reason, now)
        }

        // 2. Default Policy: Recognize the rejection fact
        return IntelligenceCommandRejectionResult(
            operationId = operationId,
            reason = reason,
            rejectedAt = now
        )
    }

    private fun handleReentryRejection(
        identity: String,
        reason: String,
        now: Instant
    ) {
        // Step 91: Formalize refusal in domain ledger
        val domainRejection = IntelligenceReentryConsumerAcknowledgement(
            reentryIdentity = identity,
            consumer = IntelligenceConsumerIdentity.INTELLIGENCE_SYNC,
            status = ReentryAcknowledgementStatus.REJECTED,
            timestamp = now,
            reason = "Contract Rejection: $reason (Recorded via Step 126)"
        )

        reentryAcknowledgementBoundary.acknowledge(domainRejection)
    }
}
