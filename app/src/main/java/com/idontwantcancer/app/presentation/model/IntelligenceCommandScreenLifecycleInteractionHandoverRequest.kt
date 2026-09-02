package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary

/**
 * Formalized request for transitioning from a lifecycle-aware screen 
 * to a user interaction. Established in Step 199.
 */
data class IntelligenceCommandScreenLifecycleInteractionHandoverRequest(
    val interaction: IntelligenceUiInteraction,
    val handler: IntelligenceInteractionBoundary,
    val onNavigate: (Any) -> Unit
)
