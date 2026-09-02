package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryTransitionCompletionResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the application component responsible for authorized 
 * consumption of verified re-entry lifecycle completions.
 * This boundary ensures that consumers receive immutable, verified results.
 */
interface IntelligenceReentryCompletionConsumptionBoundary {
    /**
     * Provides a stream of verified re-entry completion results for observation.
     */
    val completions: Flow<IntelligenceReentryTransitionCompletionResult>
}
