package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from authorization decision to execution.
 * Established in Step 186.
 */
data class IntelligenceCommandAuthorizationExecutionHandoverRequest(
    val dispatchRequest: IntelligenceCommandDispatchRequest,
    val authorizationResult: IntelligenceCommandAuthorizationResult
)
