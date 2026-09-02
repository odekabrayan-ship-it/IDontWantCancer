package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionResultHandoverRequest

/**
 * Authoritative boundary for ensuring that an execution outcome (Step 156) 
 * correctly reaches the result authority (Step 157).
 */
interface IntelligenceCommandExecutionResultBridgeBoundary {
    /**
     * Routes an execution outcome to the result authority.
     *
     * @param request The formalized handover request from Step 187.
     * @return The authoritative command result established.
     */
    fun routeToResult(
        request: IntelligenceCommandExecutionResultHandoverRequest
    ): IntelligenceApplicationCommandResult
}
