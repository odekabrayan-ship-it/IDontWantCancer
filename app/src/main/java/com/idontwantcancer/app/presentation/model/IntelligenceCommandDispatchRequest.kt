package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary

/**
 * Formalized request for command dispatch, established in Step 154.
 * Integrates an interpreted interaction with its authoritative handler and navigation path.
 */
data class IntelligenceCommandDispatchRequest(
    val interaction: IntelligenceUiInteraction,
    val handler: IntelligenceInteractionBoundary,
    val onNavigate: (Any) -> Unit
)
