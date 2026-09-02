package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import javax.inject.Inject

/**
 * Default implementation of the command timeout boundary.
 * Documents the existing lack of an application-wide command timeout policy.
 */
class DefaultIntelligenceCommandTimeoutBoundary @Inject constructor() : 
    IntelligenceCommandTimeoutBoundary {

    override fun getTimeoutPolicy(interaction: IntelligenceUiInteraction): Long? {
        // As of Step 116, no authoritative application-level command timeout policy exists.
        // Network-level and database-level timeouts are handled by their respective 
        // specialized infrastructures.
        return null
    }
}
