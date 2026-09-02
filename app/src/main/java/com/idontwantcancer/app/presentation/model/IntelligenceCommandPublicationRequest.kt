package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for command result publication, established in Step 159.
 * Encapsulates the verified result for distribution.
 */
data class IntelligenceCommandPublicationRequest(
    val result: IntelligenceApplicationCommandResult,
    val verificationResult: IntelligenceCommandVerificationResult
)
