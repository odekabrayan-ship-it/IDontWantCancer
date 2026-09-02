package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from projection to presentation.
 * Established in Step 196.
 */
data class IntelligenceCommandProjectionPresentationHandoverRequest(
    val projection: CommandConsumptionFinalityUiState
)
