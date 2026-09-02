package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command failure boundary.
 * Provides deterministic mapping of exceptions to structured results.
 */
class DefaultIntelligenceCommandFailureBoundary @Inject constructor() : 
    IntelligenceCommandFailureBoundary {

    override fun mapFailure(
        throwable: Throwable,
        operationId: String?
    ): IntelligenceApplicationCommandResult.Failure {
        // Deterministic mapping of technical failure to structured result.
        // It avoids leaking raw stack traces to the UI level.
        return IntelligenceApplicationCommandResult.Failure(
            reason = throwable.message ?: "An unhandled command-processing error occurred.",
            operationId = operationId,
            processedAt = Instant.now()
        )
    }
}
