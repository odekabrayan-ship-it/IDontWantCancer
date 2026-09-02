package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizedExecutionRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that an AUTHORIZED action reaches 
 * the existing execution authority exactly once.
 */
interface IntelligenceCommandResultClosureConsumptionFinalityExecutionBoundary {
    /**
     * Performs the authorized operation.
     *
     * @param request The formalized execution request from Step 156.
     */
    fun execute(
        request: IntelligenceCommandAuthorizedExecutionRequest
    )
}
