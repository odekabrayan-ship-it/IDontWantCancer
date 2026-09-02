package com.idontwantcancer.app.presentation.model

/**
 * Formalized request for command result finality evaluation, established in Step 163.
 * Encapsulates a closed result for terminality establishment.
 */
data class IntelligenceCommandFinalityRequest(
    val closure: IntelligenceCommandClosureResult
)
