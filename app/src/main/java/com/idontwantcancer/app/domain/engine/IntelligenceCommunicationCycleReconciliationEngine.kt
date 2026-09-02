package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import com.idontwantcancer.app.domain.model.IntelligenceBriefingReconciliationResult

/**
 * Interface for the intelligence component responsible for comparing historical 
 * communication snapshots against current authoritative intelligence.
 */
interface IntelligenceCommunicationCycleReconciliationEngine {
    /**
     * Reconciles a historical briefing with current intelligence.
     *
     * @param historicalBriefing The historical snapshot.
     * @return The structured reconciliation result.
     */
    suspend fun reconcileCycle(
        historicalBriefing: IntelligenceBriefing
    ): IntelligenceBriefingReconciliationResult
}
