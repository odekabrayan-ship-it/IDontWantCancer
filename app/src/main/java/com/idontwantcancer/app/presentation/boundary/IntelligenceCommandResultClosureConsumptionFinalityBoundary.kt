package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultClosureConsumptionAcknowledgementResult

/**
 * Authoritative boundary for determining whether downstream consumption 
 * of an already-closed result-delivery lifecycle has reached its terminal state.
 */
interface IntelligenceCommandResultClosureConsumptionFinalityBoundary {
    /**
     * Evaluates whether consumption has reached finality.
     *
     * @param closure The authoritative closure fact (Step 128).
     * @param acknowledgement The downstream acknowledgement (Step 131).
     * @return The structured finality result.
     */
    fun evaluateFinality(
        closure: IntelligenceCommandClosureResult,
        acknowledgement: IntelligenceCommandResultClosureConsumptionAcknowledgementResult
    ): IntelligenceCommandConsumptionFinalityResult
}
