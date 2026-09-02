package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from rendering to lifecycle-aware participation.
 * Established in Step 198.
 */
data class IntelligenceCommandRenderingLifecycleHandoverRequest(
    val contract: CommandConsumptionFinalityPresentationContract
)
