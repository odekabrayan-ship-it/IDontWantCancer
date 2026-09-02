package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for rendering a presentation contract into pixels.
 * Established in Step 167.
 */
data class IntelligenceCommandPresentationRenderingRequest(
    val contract: CommandConsumptionFinalityPresentationContract
)
