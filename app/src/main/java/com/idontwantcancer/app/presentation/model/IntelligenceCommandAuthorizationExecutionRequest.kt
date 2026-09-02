package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from authorization to execution.
 * Established in Step 171.
 */
data class IntelligenceCommandAuthorizationExecutionRequest(
    val dispatchRequest: IntelligenceCommandDispatchRequest,
    val authorizationResult: IntelligenceCommandAuthorizationResult
)
