package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from presentation contract establishment to rendering.
 * Established in Step 197.
 */
data class IntelligenceCommandPresentationRenderingHandoverRequest(
    val contract: CommandConsumptionFinalityPresentationContract
)
