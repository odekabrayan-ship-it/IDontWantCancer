package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.BriefingItemReconciliationDiff
import com.idontwantcancer.app.domain.model.IntelligenceReentryResult
import com.idontwantcancer.app.domain.model.Signal

/**
 * Interface for the intelligence component responsible for determining 
 * if a reconciled change qualifies to re-enter the communication pipeline.
 */
interface IntelligenceChangeReentryGate {
    /**
     * Evaluates the re-entry eligibility for a specific intelligence item change.
     *
     * @param diff The structured reconciliation difference for the item.
     * @param currentSignal The current authoritative signal, if available.
     * @return The structured re-entry result.
     */
    fun evaluateReentry(
        diff: BriefingItemReconciliationDiff,
        currentSignal: Signal?
    ): IntelligenceReentryResult
}
