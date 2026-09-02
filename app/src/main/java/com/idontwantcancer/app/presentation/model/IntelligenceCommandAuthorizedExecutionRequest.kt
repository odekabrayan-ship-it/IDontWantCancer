package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary

/**
 * Formalized request for authorized command execution, established in Step 156.
 * Binds an interaction and its handler to a verified authorization result.
 */
data class IntelligenceCommandAuthorizedExecutionRequest(
    val interaction: IntelligenceUiInteraction,
    val handler: IntelligenceInteractionBoundary,
    val authorizationResult: IntelligenceCommandAuthorizationResult
)
