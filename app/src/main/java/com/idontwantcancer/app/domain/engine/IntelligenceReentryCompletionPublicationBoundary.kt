package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryTransitionCompletionResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the intelligence component responsible for publishing 
 * verified re-entry completion results to the application.
 */
interface IntelligenceReentryCompletionPublicationBoundary {
    /**
     * Publishes a terminal completion result to authorized consumers.
     *
     * @param result The terminal completion result from Step 100.
     */
    suspend fun publishCompletion(
        result: IntelligenceReentryTransitionCompletionResult
    )

    /**
     * Provides a stream of published re-entry completions for observation.
     */
    val completionStream: Flow<IntelligenceReentryTransitionCompletionResult>
}
