package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for command result consumption, established in Step 160.
 * Encapsulates a published result for downstream availability.
 */
data class IntelligenceCommandConsumptionRequest(
    val result: IntelligenceApplicationCommandResult,
    val publicationResult: IntelligenceCommandPublicationResult
)
