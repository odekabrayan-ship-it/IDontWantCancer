package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryTransitionAuthorization
import com.idontwantcancer.app.domain.model.ReentryTransitionResult

/**
 * Interface for the intelligence component responsible for executing 
 * already-authorized lifecycle transitions.
 */
interface IntelligenceReentryTransitionExecutionBoundary {
    /**
     * Executes an authorized transition by applying it to the lifecycle authority.
     *
     * @param authorization The Step 97 authorization record.
     * @return The structured transition result.
     */
    suspend fun executeTransition(
        authorization: IntelligenceReentryTransitionAuthorization
    ): ReentryTransitionResult
}
