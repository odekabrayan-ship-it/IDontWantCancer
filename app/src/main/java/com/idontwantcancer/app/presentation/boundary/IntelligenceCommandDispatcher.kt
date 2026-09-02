package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Interface for the component responsible for routing UI interactions to 
 * the existing authoritative application commands or navigation.
 */
interface IntelligenceCommandDispatcher {
    /**
     * Dispatches a formalized UI interaction request.
     *
     * @param request The dispatch request from Step 154.
     */
    fun dispatch(request: IntelligenceCommandDispatchRequest)
}
