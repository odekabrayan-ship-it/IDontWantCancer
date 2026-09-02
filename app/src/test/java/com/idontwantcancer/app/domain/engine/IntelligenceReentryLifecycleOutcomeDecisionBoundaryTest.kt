package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryLifecycleOutcomeDecisionBoundaryTest {

    private val boundary = DefaultIntelligenceReentryLifecycleOutcomeDecisionBoundary()

    @Test
    fun `reconciled outcome is eligible for consideration`() {
        val authorized = IntelligenceReentryAuthorizedOutcome(
            outcome = IntelligenceReentryReconciliationOutcome(
                reentryIdentity = "sig1::e2",
                status = AcknowledgementReconciliationStatus.RECONCILED
            ),
            isAuthorizedForOrchestration = true,
            authorizedAt = Instant.now()
        )

        val result = boundary.evaluateEligibility(authorized)

        assertEquals(IntelligenceReentryDecisionEligibilityStatus.ELIGIBLE_FOR_CONSIDERATION, result.status)
    }

    @Test
    fun `non-reconciled outcome is not eligible for consideration`() {
        val authorized = IntelligenceReentryAuthorizedOutcome(
            outcome = IntelligenceReentryReconciliationOutcome(
                reentryIdentity = "sig1::e2",
                status = AcknowledgementReconciliationStatus.NOT_RECONCILED,
                reason = "Mismatch"
            ),
            isAuthorizedForOrchestration = true,
            authorizedAt = Instant.now()
        )

        val result = boundary.evaluateEligibility(authorized)

        assertEquals(IntelligenceReentryDecisionEligibilityStatus.NOT_ELIGIBLE_FOR_CONSIDERATION, result.status)
    }

    @Test
    fun `unauthorized outcome is not eligible for consideration`() {
        val authorized = IntelligenceReentryAuthorizedOutcome(
            outcome = IntelligenceReentryReconciliationOutcome(
                reentryIdentity = "sig1::e2",
                status = AcknowledgementReconciliationStatus.RECONCILED
            ),
            isAuthorizedForOrchestration = false, // Explicitly unauthorized
            authorizedAt = Instant.now()
        )

        val result = boundary.evaluateEligibility(authorized)

        assertEquals(IntelligenceReentryDecisionEligibilityStatus.NOT_ELIGIBLE_FOR_CONSIDERATION, result.status)
    }
}
