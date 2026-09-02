package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from result establishment to verification.
 * Established in Step 173.
 */
data class IntelligenceCommandResultVerificationRequest(
    val result: IntelligenceApplicationCommandResult
)
