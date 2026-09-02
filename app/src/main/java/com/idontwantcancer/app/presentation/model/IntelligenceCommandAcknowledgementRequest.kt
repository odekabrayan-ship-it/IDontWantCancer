package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for command result acknowledgement, established in Step 161.
 * Encapsulates a result that has been consumed for downstream confirmation.
 */
data class IntelligenceCommandAcknowledgementRequest(
    val result: IntelligenceApplicationCommandResult
)
