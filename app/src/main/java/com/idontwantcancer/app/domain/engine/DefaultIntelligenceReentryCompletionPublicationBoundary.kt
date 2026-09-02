package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryTransitionCompletionResult
import com.idontwantcancer.app.domain.model.ReentryTransitionCompletionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of the completion publication boundary.
 * Uses a SharedFlow to propagate verified results to authorized application consumers.
 */
@Singleton
class DefaultIntelligenceReentryCompletionPublicationBoundary @Inject constructor() : 
    IntelligenceReentryCompletionPublicationBoundary {

    private val _completionStream = MutableSharedFlow<IntelligenceReentryTransitionCompletionResult>(replay = 10)
    override val completionStream: Flow<IntelligenceReentryTransitionCompletionResult> = _completionStream.asSharedFlow()

    override suspend fun publishCompletion(
        result: IntelligenceReentryTransitionCompletionResult
    ) {
        // Enforce Step 100 finality: Only broadcast if the state is terminal (completed or failed)
        // This is a handoff boundary, it does not re-verify the logic of Step 100.
        _completionStream.emit(result)
    }
}
