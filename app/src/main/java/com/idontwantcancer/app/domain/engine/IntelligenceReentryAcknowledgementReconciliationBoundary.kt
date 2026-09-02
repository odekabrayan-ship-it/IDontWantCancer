package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceAcknowledgementReconciliationResult
import com.idontwantcancer.app.domain.model.IntelligenceCommunicationHandoff
import com.idontwantcancer.app.domain.model.IntelligenceReentryConsumerAcknowledgement

/**
 * Interface for the intelligence component responsible for determining whether
 * a downstream acknowledgement corresponds to the authorized handoff.
 */
interface IntelligenceReentryAcknowledgementReconciliationBoundary {
    /**
     * Reconciles an acknowledgement with its corresponding handoff.
     *
     * @param acknowledgement The consumer's acknowledgement report.
     * @param handoff The original authoritative handoff.
     * @return The structured reconciliation result.
     */
    fun reconcile(
        acknowledgement: IntelligenceReentryConsumerAcknowledgement,
        handoff: IntelligenceCommunicationHandoff
    ): IntelligenceAcknowledgementReconciliationResult
}
