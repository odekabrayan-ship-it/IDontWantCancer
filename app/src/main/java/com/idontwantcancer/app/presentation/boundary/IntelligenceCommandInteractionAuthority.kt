package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingInteractionRequest

/**
 * Authoritative boundary for interpreting user interactions and 
 * coordinating the entry into the command reporting loop.
 */
interface IntelligenceCommandInteractionAuthority {
    /**
     * Handles a formalized user interaction request.
     *
     * @param request The formalized interaction request from Step 168.
     */
    fun handleInteraction(
        request: IntelligenceCommandRenderingInteractionRequest
    )
}
