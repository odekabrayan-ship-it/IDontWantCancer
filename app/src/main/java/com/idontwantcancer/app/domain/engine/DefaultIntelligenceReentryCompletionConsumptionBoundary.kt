package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryTransitionCompletionResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Default implementation of the completion consumption boundary.
 * Delegates to the Step 101 publication stream to maintain the causal chain.
 */
class DefaultIntelligenceReentryCompletionConsumptionBoundary @Inject constructor(
    private val publicationBoundary: IntelligenceReentryCompletionPublicationBoundary
) : IntelligenceReentryCompletionConsumptionBoundary {

    override val completions: Flow<IntelligenceReentryTransitionCompletionResult> = 
        publicationBoundary.completionStream
}
