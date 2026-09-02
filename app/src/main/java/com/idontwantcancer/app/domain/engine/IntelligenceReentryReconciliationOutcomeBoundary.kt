package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceAcknowledgementReconciliationResult
import com.idontwantcancer.app.domain.model.IntelligenceReentryReconciliationOutcome

/**
 * Interface for the intelligence component responsible for defining the 
 * authorized next system-level outcome after reconciliation.
 */
interface IntelligenceReentryReconciliationOutcomeBoundary {
    /**
     * Determines the formal outcome from a reconciliation result.
     *
     * @param result The result of the reconciliation boundary (Step 92).
     * @return The structured outcome (Step 93).
     */
    fun determineOutcome(
        result: IntelligenceAcknowledgementReconciliationResult
    ): IntelligenceReentryReconciliationOutcome
}
