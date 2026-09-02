package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandRejectionResult

/**
 * Authoritative boundary for recording or communicating that an 
 * already-acknowledged result is NOT accepted under the receiving contract.
 */
interface IntelligenceCommandResultRejectionBoundary {
    /**
     * Records a formal rejection for a handed-off command result.
     *
     * @param result The finalized result that was rejected.
     * @param reason The reason for rejection based on the destination contract.
     * @return The structured rejection result.
     */
    suspend fun recordRejection(
        result: IntelligenceApplicationCommandResult,
        reason: String
    ): IntelligenceCommandRejectionResult
}
