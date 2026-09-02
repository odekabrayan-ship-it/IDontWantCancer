package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from finality establishment to terminal integrity.
 * Established in Step 194.
 */
data class IntelligenceCommandFinalityTerminalIntegrityHandoverRequest(
    val finalityResult: IntelligenceCommandConsumptionFinalityResult
)
