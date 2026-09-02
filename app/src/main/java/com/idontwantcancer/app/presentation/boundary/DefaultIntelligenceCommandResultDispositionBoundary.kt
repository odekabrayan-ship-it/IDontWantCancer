package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command result disposition boundary.
 * Aggregates acknowledgement and acceptance results into a final terminal 
 * receiving-side state.
 */
class DefaultIntelligenceCommandResultDispositionBoundary @Inject constructor(
    private val acknowledgementHandoverBridge: IntelligenceCommandConsumptionAcknowledgementHandoverBoundary,
    private val acceptanceBoundary: IntelligenceCommandResultAcceptanceBoundary
) : IntelligenceCommandResultDispositionBoundary {

    override suspend fun evaluateDisposition(
        result: IntelligenceApplicationCommandResult
    ): IntelligenceCommandDispositionResult {
        val now = Instant.now()
        val operationId = result.operationId ?: "anonymous"

        // 1. Check Acknowledgement Bridge (Step 124 / 161 / 176 / 191 / 192)
        // Construct a non-persisting consumption context for the acknowledgement bridge.
        val consumptionRequest = IntelligenceCommandConsumptionRequest(
            result = result,
            publicationResult = IntelligenceCommandPublicationResult(
                operationId = operationId,
                status = CommandPublicationStatus.PUBLISHED,
                reason = "Derived for disposition evaluation."
            )
        )
        val ackHandoverRequest = IntelligenceCommandConsumptionAcknowledgementHandoverRequest(result, consumptionRequest)
        val ack = acknowledgementHandoverBridge.routeToAcknowledgement(ackHandoverRequest)
        
        // 2. Check Acceptance (Step 125)
        val acceptance = acceptanceBoundary.evaluateAcceptance(result)

        // 3. Determine Final Disposition
        val disposition = when {
            acceptance.status == CommandAcceptanceStatus.ACCEPTED -> CommandResultDisposition.ACCEPTED
            acceptance.status == CommandAcceptanceStatus.REJECTED -> CommandResultDisposition.REJECTED
            ack.status == CommandAcknowledgementStatus.ACKNOWLEDGED -> CommandResultDisposition.ACKNOWLEDGED
            else -> CommandResultDisposition.UNRESOLVED
        }

        return IntelligenceCommandDispositionResult(
            operationId = operationId,
            disposition = disposition,
            reason = acceptance.reason ?: ack.reason ?: "Disposition establishment complete.",
            evaluatedAt = now
        )
    }
}
