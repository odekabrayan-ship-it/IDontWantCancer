package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.BriefingItemReconciliationDiff
import com.idontwantcancer.app.domain.model.IntelligenceReentryDeduplicationResult
import com.idontwantcancer.app.domain.model.IntelligenceReentryResult

/**
 * Interface for the intelligence component responsible for ensuring that 
 * re-entry events are not processed multiple times.
 */
interface IntelligenceReentryDeduplicationGate {
    /**
     * Checks whether the provided re-entry candidate has already been admitted.
     *
     * @param reentryResult The result from the re-entry eligibility gate.
     * @param diff The structured diff that triggered the re-entry evaluation.
     * @return The structured deduplication result.
     */
    suspend fun checkDeduplication(
        reentryResult: IntelligenceReentryResult,
        diff: BriefingItemReconciliationDiff
    ): IntelligenceReentryDeduplicationResult
}
