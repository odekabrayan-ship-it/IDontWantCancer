package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from acknowledgement to closure.
 * Established in Step 192.
 */
data class IntelligenceCommandAcknowledgementClosureHandoverRequest(
    val result: IntelligenceApplicationCommandResult,
    val acknowledgementResult: IntelligenceCommandAcknowledgementResult
)
