package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command result closure boundary.
 * Determines terminality based on authoritative receiving-side disposition.
 */
class DefaultIntelligenceCommandResultClosureBoundary @Inject constructor(
    private val dispositionBoundary: IntelligenceCommandResultDispositionBoundary
) : IntelligenceCommandResultClosureBoundary, 
    IntelligenceCommandResultAcknowledgementClosureBoundary {

    override suspend fun evaluateClosure(
        request: IntelligenceCommandAcknowledgementClosureHandoverRequest
    ): IntelligenceCommandClosureResult {
        // Step 147 / 162 / 177 / 192 Logic: Ensure closure is evaluated ONLY after acknowledgement.
        // It delegates the actual terminal logic to the existing Step 128 authority.
        return evaluateClosure(request.result)
    }

    override suspend fun evaluateClosure(
        result: IntelligenceApplicationCommandResult
    ): IntelligenceCommandClosureResult {
        val now = Instant.now()
        val operationId = result.operationId ?: "anonymous"

        // 1. Establish Receiving-Side Disposition (Step 127)
        val dispositionResult = dispositionBoundary.evaluateDisposition(result)

        // 2. Determine Closure (Terminal State)
        val status = when (dispositionResult.disposition) {
            CommandResultDisposition.ACCEPTED,
            CommandResultDisposition.REJECTED -> CommandResultClosureStatus.CLOSED
            
            CommandResultDisposition.ACKNOWLEDGED,
            CommandResultDisposition.UNRESOLVED -> CommandResultClosureStatus.OPEN
        }

        return IntelligenceCommandClosureResult(
            operationId = operationId,
            status = status,
            reason = "Disposition established as ${dispositionResult.disposition}; closure transition complete.",
            evaluatedAt = now
        )
    }
}
