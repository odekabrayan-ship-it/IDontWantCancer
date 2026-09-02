package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from dispatch to authorization.
 * Established in Step 185.
 */
data class IntelligenceCommandDispatchAuthorizationHandoverRequest(
    val dispatchRequest: IntelligenceCommandDispatchRequest
)
