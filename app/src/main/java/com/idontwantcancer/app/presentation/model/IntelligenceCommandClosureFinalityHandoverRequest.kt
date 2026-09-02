package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from closure to finality.
 * Established in Step 193.
 */
data class IntelligenceCommandClosureFinalityHandoverRequest(
    val closure: IntelligenceCommandClosureResult
)
