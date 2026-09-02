package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary

/**
 * Formalized request for transitioning from a rendered user action to the interaction authority.
 * Established in Step 183.
 */
data class IntelligenceCommandRenderingInteractionHandoverRequest(
    val interaction: IntelligenceUiInteraction,
    val handler: IntelligenceInteractionBoundary,
    val onNavigate: (Any) -> Unit
)
