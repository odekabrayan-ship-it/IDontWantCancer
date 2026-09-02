package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from dispatch to authorization.
 * Established in Step 170.
 */
data class IntelligenceCommandDispatchAuthorizationRequest(
    val dispatchRequest: IntelligenceCommandDispatchRequest
)
