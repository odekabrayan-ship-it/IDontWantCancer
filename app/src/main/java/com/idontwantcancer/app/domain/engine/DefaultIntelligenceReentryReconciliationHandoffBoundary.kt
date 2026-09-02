package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryApplicationStateReconciliationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of the reconciliation handoff boundary.
 * Provides a secure, reactive channel for propagating reconciliation outcomes.
 */
@Singleton
class DefaultIntelligenceReentryReconciliationHandoffBoundary @Inject constructor() : 
    IntelligenceReentryReconciliationHandoffBoundary {

    private val _outcomeStream = MutableSharedFlow<IntelligenceReentryApplicationStateReconciliationResult>(replay = 10)
    override val outcomeStream: Flow<IntelligenceReentryApplicationStateReconciliationResult> = _outcomeStream.asSharedFlow()

    override suspend fun handoffResult(
        result: IntelligenceReentryApplicationStateReconciliationResult
    ) {
        // Enforce the causal chain: Only authorized outcomes reach this boundary.
        // It does not reinterpret or repair; it only hands off the fact.
        _outcomeStream.emit(result)
    }

    override fun getLatestResult(reentryIdentity: String): IntelligenceReentryApplicationStateReconciliationResult? {
        return _outcomeStream.replayCache.find { it.reentryIdentity == reentryIdentity }
    }
}
