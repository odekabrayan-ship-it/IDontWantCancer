package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult

/**
 * Authoritative boundary for ensuring that an established finality result (Step 178) 
 * correctly reaches the terminal-integrity authority (Step 179).
 */
interface IntelligenceCommandFinalityTerminalIntegrityHandoverBoundary {
    /**
     * Routes an established finality result to the terminal-integrity authority.
     *
     * @param finalityResult The authoritative finality record.
     * @return The verified/protected finality result.
     */
    fun routeToIntegrity(
        finalityResult: IntelligenceCommandConsumptionFinalityResult
    ): IntelligenceCommandConsumptionFinalityResult
}
