package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityUiState
import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
import kotlinx.coroutines.flow.Flow

/**
 * Boundary interface for projecting authoritative consumption finality into 
 * application-level read models.
 */
interface CommandConsumptionFinalityProjectionBoundary {
    /**
     * A stream of read-only finality projections.
     */
    val finalityProjectionStream: Flow<CommandConsumptionFinalityUiState>

    /**
     * Projects a validated terminal state into the read model.
     *
     * @param request The formalized projection request from Step 195.
     */
    fun projectTerminality(
        request: IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
    )
}
