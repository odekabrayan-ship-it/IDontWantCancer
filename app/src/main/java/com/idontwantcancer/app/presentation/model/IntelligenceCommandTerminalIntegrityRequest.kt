package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for protecting the integrity of a terminal finality result.
 * Established in Step 164.
 */
data class IntelligenceCommandTerminalIntegrityRequest(
    val finalityResult: IntelligenceCommandConsumptionFinalityResult
)
