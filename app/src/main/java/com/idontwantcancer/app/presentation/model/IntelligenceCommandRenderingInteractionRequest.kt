package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.presentation.boundary.IntelligenceInteractionBoundary

/**
 * Formalized request for capturing a rendered user action and routing it 
 * to the interaction authority. Established in Step 168.
 */
data class IntelligenceCommandRenderingInteractionRequest(
    val interaction: IntelligenceUiInteraction,
    val handler: IntelligenceInteractionBoundary,
    val onNavigate: (Any) -> Unit
)
