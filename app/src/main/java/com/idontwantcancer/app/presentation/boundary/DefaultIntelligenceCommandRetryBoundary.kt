package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandRetryStatus
import com.idontwantcancer.app.presentation.model.IntelligenceCommandRetryResult
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command retry boundary.
 * Documents and enforces existing retry policies.
 */
class DefaultIntelligenceCommandRetryBoundary @Inject constructor() : 
    IntelligenceCommandRetryBoundary {

    override fun evaluateRetry(
        interaction: IntelligenceUiInteraction,
        throwable: Throwable,
        currentAttempt: Int
    ): IntelligenceCommandRetryResult {
        val now = Instant.now()

        // 1. UI-Driven Retry Policy
        // Standard UI interactions rely on the User as the authoritative retry engine.
        // The UI renders an error state with a "Try again" button.
        val status = when (interaction) {
            is IntelligenceUiInteraction.RetryOperation,
            is IntelligenceUiInteraction.PerformSearch,
            is IntelligenceUiInteraction.ViewSignalDetails,
            is IntelligenceUiInteraction.NavigateBack,
            is IntelligenceUiInteraction.ClearSearch,
            is IntelligenceUiInteraction.CancelOperation,
            is IntelligenceUiInteraction.AcknowledgeSignal,
            is IntelligenceUiInteraction.ToggleWatch,
            is IntelligenceUiInteraction.ClearSelection -> {
                CommandRetryStatus.EXTERNALLY_MANAGED
            }
        }

        return IntelligenceCommandRetryResult(
            status = status,
            reason = "Retry is managed by the external authority (User/UI).",
            attemptCount = currentAttempt,
            evaluatedAt = now
        )
    }
}
