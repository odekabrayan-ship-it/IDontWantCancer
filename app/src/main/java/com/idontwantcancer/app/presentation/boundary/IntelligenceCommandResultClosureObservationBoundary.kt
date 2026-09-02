package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary for observing the closure status of command result deliveries.
 * Provides read-only access to terminal lifecycle events for downstream components.
 */
interface IntelligenceCommandResultClosureObservationBoundary {
    /**
     * A stream of command closure results.
     * Consumers can observe this to know when an operation's delivery lifecycle has concluded.
     */
    val closureStream: Flow<IntelligenceCommandClosureResult>
}
