package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary for observing the finality of closure consumption.
 * Provides read-only access to terminal consumption events for downstream components.
 */
interface IntelligenceCommandResultClosureConsumptionFinalityObservationBoundary {
    /**
     * A stream of command consumption finality results.
     * Consumers can observe this to know when the downstream processing has reached 
     * its authoritative terminal state.
     */
    val finalityStream: Flow<IntelligenceCommandConsumptionFinalityResult>
}
