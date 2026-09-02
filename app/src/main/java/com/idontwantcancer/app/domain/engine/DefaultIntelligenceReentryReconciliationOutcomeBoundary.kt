package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import javax.inject.Inject

/**
 * Default implementation of the reconciliation outcome boundary.
 * Provides a deterministic mapping from reconciliation results to system outcomes.
 */
class DefaultIntelligenceReentryReconciliationOutcomeBoundary @Inject constructor() : 
    IntelligenceReentryReconciliationOutcomeBoundary {

    override fun determineOutcome(
        result: IntelligenceAcknowledgementReconciliationResult
    ): IntelligenceReentryReconciliationOutcome {
        return IntelligenceReentryReconciliationOutcome(
            reentryIdentity = result.reentryIdentity,
            status = result.status,
            reason = result.mismatchReason
        )
    }
}
