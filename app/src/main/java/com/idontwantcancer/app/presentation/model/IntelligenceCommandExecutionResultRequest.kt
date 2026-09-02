package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for capturing an execution outcome and routing it 
 * to the result authority. Established in Step 172.
 */
data class IntelligenceCommandExecutionResultRequest(
    val interaction: IntelligenceUiInteraction,
    val outcome: IntelligenceCommandExecutionOutcome
)
