package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from verification to publication.
 * Established in Step 189.
 */
data class IntelligenceCommandVerificationPublicationHandoverRequest(
    val result: IntelligenceApplicationCommandResult,
    val verificationResult: IntelligenceCommandVerificationResult
)
