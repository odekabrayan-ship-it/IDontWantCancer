package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from terminal integrity protection to projection.
 * Established in Step 195.
 */
data class IntelligenceCommandTerminalIntegrityProjectionHandoverRequest(
    val finalityResult: IntelligenceCommandConsumptionFinalityResult
)
