package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for transitioning from an execution outcome to the result authority.
 * Established in Step 187.
 */
data class IntelligenceCommandExecutionResultHandoverRequest(
    val interaction: IntelligenceUiInteraction,
    val outcome: IntelligenceCommandExecutionOutcome
)
