package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command result boundary.
 * Produces deterministic, immutable result models.
 */
class DefaultIntelligenceCommandResultBoundary @Inject constructor() : 
    IntelligenceCommandResultBoundary {

    override fun success(operationId: String?): IntelligenceApplicationCommandResult.Success {
        return IntelligenceApplicationCommandResult.Success(
            operationId = operationId,
            processedAt = Instant.now()
        )
    }

    override fun failure(reason: String, operationId: String?): IntelligenceApplicationCommandResult.Failure {
        return IntelligenceApplicationCommandResult.Failure(
            reason = reason,
            operationId = operationId,
            processedAt = Instant.now()
        )
    }

    override fun rejected(reason: String, operationId: String?): IntelligenceApplicationCommandResult.Rejected {
        return IntelligenceApplicationCommandResult.Rejected(
            reason = reason,
            operationId = operationId,
            processedAt = Instant.now()
        )
    }

    override fun cancelled(operationId: String?): IntelligenceApplicationCommandResult.Cancelled {
        return IntelligenceApplicationCommandResult.Cancelled(
            operationId = operationId,
            processedAt = Instant.now()
        )
    }

    override fun timedOut(operationId: String?): IntelligenceApplicationCommandResult.TimedOut {
        return IntelligenceApplicationCommandResult.TimedOut(
            operationId = operationId,
            processedAt = Instant.now()
        )
    }
}
