package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryApplicationStateReconciliationResult
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative, read-only interface for observing re-entry reconciliation outcomes.
 * Allows application components to passively react to the state of scientific updates.
 */
interface IntelligenceReentryStateObserver {
    /**
     * A stream of verified re-entry application-state reconciliation outcomes.
     */
    val reconciliationOutcomes: Flow<IntelligenceReentryApplicationStateReconciliationResult>
}
