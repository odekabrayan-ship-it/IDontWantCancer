package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from consumption to acknowledgement.
 * Established in Step 191.
 */
data class IntelligenceCommandConsumptionAcknowledgementHandoverRequest(
    val result: IntelligenceApplicationCommandResult,
    val consumptionRequest: IntelligenceCommandConsumptionRequest
)
