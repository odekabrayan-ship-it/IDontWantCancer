package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the reconciliation boundary that verifies
 * the deterministic correlation between acknowledgements and handoffs.
 */
class DefaultIntelligenceReentryAcknowledgementReconciliationBoundary @Inject constructor() : 
    IntelligenceReentryAcknowledgementReconciliationBoundary {

    override fun reconcile(
        acknowledgement: IntelligenceReentryConsumerAcknowledgement,
        handoff: IntelligenceCommunicationHandoff
    ): IntelligenceAcknowledgementReconciliationResult {
        val now = Instant.now()
        val handoffReentry = handoff.reentryHandoff

        // 1. Identity Correlation Check
        val identityMatch = acknowledgement.reentryIdentity == handoffReentry?.reentryIdentity

        // 2. Logic Check
        val status = if (identityMatch && handoffReentry != null) {
            AcknowledgementReconciliationStatus.RECONCILED
        } else {
            AcknowledgementReconciliationStatus.NOT_RECONCILED
        }

        return IntelligenceAcknowledgementReconciliationResult(
            reentryIdentity = acknowledgement.reentryIdentity,
            status = status,
            consumer = acknowledgement.consumer,
            mismatchReason = if (!identityMatch) "Identity mismatch: ${acknowledgement.reentryIdentity} vs ${handoffReentry?.reentryIdentity}"
                             else if (handoffReentry == null) "Original handoff contained no re-entry context."
                             else null,
            reconciledAt = now
        )
    }
}
