package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizedExecutionRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import javax.inject.Inject

/**
 * Default implementation of the command execution boundary.
 * Bridges the gap between authorized dispatch and application-level performance.
 */
class DefaultIntelligenceCommandResultClosureConsumptionFinalityExecutionBoundary @Inject constructor() : 
    IntelligenceCommandResultClosureConsumptionFinalityExecutionBoundary {

    override fun execute(
        request: IntelligenceCommandAuthorizedExecutionRequest
    ) {
        // Step 141 / 156 Logic: Perform the authorized operation.
        // It ensures the action reaches the existing execution authority (handler)
        // only if authorization has been established.
        request.handler.onInteraction(request.interaction)
    }
}
