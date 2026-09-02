package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandFinalityRequest

/**
 * Authoritative boundary for ensuring that a closed command result 
 * correctly enters the finality authority.
 */
interface IntelligenceCommandResultClosureFinalityBoundary {
    /**
     * Evaluates finality for a closed command result.
     *
     * @param request The formalized finality request from Step 163.
     * @return The structured finality result (Step 132 product).
     */
    fun evaluateFinality(
        request: IntelligenceCommandFinalityRequest
    ): IntelligenceCommandConsumptionFinalityResult
}
