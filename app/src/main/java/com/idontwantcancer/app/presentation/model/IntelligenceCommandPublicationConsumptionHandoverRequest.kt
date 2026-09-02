package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from publication to consumption.
 * Established in Step 190.
 */
data class IntelligenceCommandPublicationConsumptionHandoverRequest(
    val result: IntelligenceApplicationCommandResult,
    val publicationResult: IntelligenceCommandPublicationResult
)
