package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary

/**
 * Formalized request for transitioning from user interaction interpreting 
 * to formal dispatch handover. Established in Step 200.
 */
data class IntelligenceCommandUserInteractionDispatchHandoverRequest(
    val interaction: IntelligenceUiInteraction,
    val handler: IntelligenceInteractionBoundary,
    val onNavigate: (Any) -> Unit
)
