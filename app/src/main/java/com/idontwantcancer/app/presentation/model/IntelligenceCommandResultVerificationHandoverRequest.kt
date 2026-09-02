package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from result establishment to verification.
 * Established in Step 188.
 */
data class IntelligenceCommandResultVerificationHandoverRequest(
    val result: IntelligenceApplicationCommandResult
)
