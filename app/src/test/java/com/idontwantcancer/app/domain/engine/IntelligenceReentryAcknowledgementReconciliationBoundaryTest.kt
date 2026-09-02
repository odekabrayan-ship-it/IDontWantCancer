package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryAcknowledgementReconciliationBoundaryTest {

    private val boundary = DefaultIntelligenceReentryAcknowledgementReconciliationBoundary()

    @Test
    fun `exact matching acknowledgement reconciles successfully`() {
        val identity = "sig1::e2"
        val handoff = createHandoff(identity)
        val acknowledgement = createAcknowledgement(identity)

        val result = boundary.reconcile(acknowledgement, handoff)

        assertEquals(AcknowledgementReconciliationStatus.RECONCILED, result.status)
    }

    @Test
    fun `mismatched identity is rejected`() {
        val handoff = createHandoff("sig1::e2")
        val acknowledgement = createAcknowledgement("sig1::e3")

        val result = boundary.reconcile(acknowledgement, handoff)

        assertEquals(AcknowledgementReconciliationStatus.NOT_RECONCILED, result.status)
    }

    private fun createHandoff(reentryIdentity: String) = IntelligenceCommunicationHandoff(
        packageId = "p1",
        intelligenceId = "sig1",
        threadId = "t1",
        communicationPackage = mockk(),
        reentryHandoff = IntelligenceReentryHandoffResult(
            reentryIdentity = reentryIdentity,
            status = IntelligenceReentryHandoffStatus.HANDOFF_ACCEPTED,
            contract = null,
            handedOffAt = Instant.now()
        ),
        authorizedAt = Instant.now()
    )

    private fun createAcknowledgement(reentryIdentity: String) = IntelligenceReentryConsumerAcknowledgement(
        reentryIdentity = reentryIdentity,
        consumer = IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY,
        status = ReentryAcknowledgementStatus.ACCEPTED,
        timestamp = Instant.now()
    )
}
