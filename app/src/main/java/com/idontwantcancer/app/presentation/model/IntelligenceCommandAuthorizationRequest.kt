package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for command dispatch authorization, established in Step 155.
 * Encapsulates the interaction and its unique identity for policy evaluation.
 */
data class IntelligenceCommandAuthorizationRequest(
    val interaction: IntelligenceUiInteraction,
    val commandIdentity: String
)
