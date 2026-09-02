package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

class IntelligenceReentryDecisionRecordBoundaryTest {

    private val boundary = DefaultIntelligenceReentryDecisionRecordBoundary()

    @Test
    fun `eligible reconciled outcome creates a COMPLETED candidate record`() {
        val identity = "sig1::e2"
        val authorized = createAuthorizedOutcome(identity, AcknowledgementReconciliationStatus.RECONCILED)
        val eligibility = createEligibility(identity, IntelligenceReentryDecisionEligibilityStatus.ELIGIBLE_FOR_CONSIDERATION)

        val record = boundary.createRecord(authorized, eligibility)

        assertEquals(DecisionRecordStatus.CANDIDATE_PRESENTED, record.status)
        assertEquals(ReentryLifecycleState.COMPLETED, record.proposedState)
        assertEquals(identity, record.reentryIdentity)
    }

    @Test
    fun `ineligible outcome results in INELIGIBLE status and no proposed state`() {
        val identity = "sig1::e2"
        val authorized = createAuthorizedOutcome(identity, AcknowledgementReconciliationStatus.NOT_RECONCILED)
        val eligibility = createEligibility(identity, IntelligenceReentryDecisionEligibilityStatus.NOT_ELIGIBLE_FOR_CONSIDERATION)

        val record = boundary.createRecord(authorized, eligibility)

        assertEquals(DecisionRecordStatus.INELIGIBLE_FOR_DECISION, record.status)
        assertNull(record.proposedState)
    }

    private fun createAuthorizedOutcome(id: String, status: AcknowledgementReconciliationStatus) = 
        IntelligenceReentryAuthorizedOutcome(
            outcome = IntelligenceReentryReconciliationOutcome(id, status),
            isAuthorizedForOrchestration = true,
            authorizedAt = Instant.now()
        )

    private fun createEligibility(id: String, status: IntelligenceReentryDecisionEligibilityStatus) = 
        IntelligenceReentryDecisionEligibilityResult(
            reentryIdentity = id,
            status = status,
            evaluatedAt = Instant.now()
        )
}
