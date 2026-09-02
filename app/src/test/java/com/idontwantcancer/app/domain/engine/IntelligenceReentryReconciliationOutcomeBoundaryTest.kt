package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryReconciliationOutcomeBoundaryTest {

    private val boundary = DefaultIntelligenceReentryReconciliationOutcomeBoundary()

    @Test
    fun `reconciled input produces RECONCILED outcome`() {
        val result = IntelligenceAcknowledgementReconciliationResult(
            reentryIdentity = "sig1::e2",
            status = AcknowledgementReconciliationStatus.RECONCILED,
            consumer = IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY,
            reconciledAt = Instant.now()
        )

        val outcome = boundary.determineOutcome(result)

        assertEquals(AcknowledgementReconciliationStatus.RECONCILED, outcome.status)
        assertEquals("sig1::e2", outcome.reentryIdentity)
    }

    @Test
    fun `non-reconciled input produces NOT_RECONCILED outcome`() {
        val result = IntelligenceAcknowledgementReconciliationResult(
            reentryIdentity = "sig1::e2",
            status = AcknowledgementReconciliationStatus.NOT_RECONCILED,
            consumer = IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY,
            mismatchReason = "ID mismatch",
            reconciledAt = Instant.now()
        )

        val outcome = boundary.determineOutcome(result)

        assertEquals(AcknowledgementReconciliationStatus.NOT_RECONCILED, outcome.status)
        assertEquals("ID mismatch", outcome.reason)
    }
}
