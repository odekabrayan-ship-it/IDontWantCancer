package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryApplicationStateReconciliationResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Default implementation of the re-entry state observer.
 * Delegates directly to the authoritative handoff boundary to ensure consistency.
 */
class DefaultIntelligenceReentryStateObserver @Inject constructor(
    private val handoffBoundary: IntelligenceReentryReconciliationHandoffBoundary
) : IntelligenceReentryStateObserver {

    override val reconciliationOutcomes: Flow<IntelligenceReentryApplicationStateReconciliationResult> = 
        handoffBoundary.outcomeStream
}
