package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IntelligenceReentryOutcomeConsumptionBoundaryTest {

    private val boundary = DefaultIntelligenceReentryOutcomeConsumptionBoundary()

    @Test
    fun `valid RECONCILED outcome can be authorized for orchestration`() {
        val outcome = IntelligenceReentryReconciliationOutcome(
            reentryIdentity = "sig1::e2",
            status = AcknowledgementReconciliationStatus.RECONCILED
        )

        val authorized = boundary.authorizeOutcome(outcome)

        assertTrue(authorized.isAuthorizedForOrchestration)
        assertEquals(outcome, authorized.outcome)
    }

    @Test
    fun `valid NOT_RECONCILED outcome can be authorized for orchestration`() {
        val outcome = IntelligenceReentryReconciliationOutcome(
            reentryIdentity = "sig1::e2",
            status = AcknowledgementReconciliationStatus.NOT_RECONCILED,
            reason = "ID Mismatch"
        )

        val authorized = boundary.authorizeOutcome(outcome)

        assertTrue(authorized.isAuthorizedForOrchestration)
        assertEquals(AcknowledgementReconciliationStatus.NOT_RECONCILED, authorized.outcome.status)
    }

    @Test
    fun `authorization is deterministic`() {
        val outcome = IntelligenceReentryReconciliationOutcome(
            reentryIdentity = "sig1::e2",
            status = AcknowledgementReconciliationStatus.RECONCILED
        )

        val authorized1 = boundary.authorizeOutcome(outcome)
        val authorized2 = boundary.authorizeOutcome(outcome)

        assertEquals(authorized1.outcome, authorized2.outcome)
        assertEquals(authorized1.isAuthorizedForOrchestration, authorized2.isAuthorizedForOrchestration)
    }
}
