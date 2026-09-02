package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the re-entry handoff boundary.
 * Consumes the isolated authorized representation to fulfill the handoff contract.
 */
class DefaultIntelligenceReentryHandoffBoundary @Inject constructor(
    private val isolatedBoundary: IntelligenceReentryIsolatedConsumerBoundary
) : IntelligenceReentryHandoffBoundary {

    override suspend fun performHandoff(
        reentryIdentity: String,
        consumer: IntelligenceConsumerIdentity
    ): IntelligenceReentryHandoffResult {
        val now = Instant.now()
        
        // 1. Obtain isolated contract (Step 88)
        val contract = isolatedBoundary.getIsolatedContract(reentryIdentity, consumer)

        return if (contract != null) {
            IntelligenceReentryHandoffResult(
                reentryIdentity = reentryIdentity,
                status = IntelligenceReentryHandoffStatus.HANDOFF_ACCEPTED,
                contract = contract,
                handedOffAt = now
            )
        } else {
            IntelligenceReentryHandoffResult(
                reentryIdentity = reentryIdentity,
                status = IntelligenceReentryHandoffStatus.HANDOFF_REJECTED,
                contract = null,
                rejectionReason = "Handoff rejected: Contract unverified or consumer unauthorized.",
                handedOffAt = now
            )
        }
    }
}
