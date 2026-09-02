package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary

/**
 * Formalized request for transitioning from an interpreted interaction to the dispatch authority.
 * Established in Step 184.
 */
data class IntelligenceCommandInteractionDispatchHandoverRequest(
    val interaction: IntelligenceUiInteraction,
    val handler: IntelligenceInteractionBoundary,
    val onNavigate: (Any) -> Unit
)
