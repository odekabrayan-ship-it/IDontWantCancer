package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceReentryTransitionGuardTest {

    private val guard = DefaultIntelligenceReentryTransitionGuard()

    @Test
    fun `ADMITTED to PROCESSING is valid`() {
        val current = createLifecycle(ReentryLifecycleState.ADMITTED)
        val result = guard.validateTransition(current, ReentryLifecycleState.PROCESSING)

        assertTrue(result is ReentryTransitionResult.Accepted)
        assertEquals(ReentryLifecycleState.PROCESSING, (result as ReentryTransitionResult.Accepted).updatedLifecycle.currentState)
    }

    @Test
    fun `PROCESSING to COMPLETED is valid`() {
        val current = createLifecycle(ReentryLifecycleState.PROCESSING)
        val result = guard.validateTransition(current, ReentryLifecycleState.COMPLETED)

        assertTrue(result is ReentryTransitionResult.Accepted)
    }

    @Test
    fun `COMPLETED to PROCESSING is invalid (terminal)`() {
        val current = createLifecycle(ReentryLifecycleState.COMPLETED)
        val result = guard.validateTransition(current, ReentryLifecycleState.PROCESSING)

        assertTrue(result is ReentryTransitionResult.Rejected)
        assertEquals(ReentryTransitionRejectionReason.TERMINAL_STATE, (result as ReentryTransitionResult.Rejected).reason)
    }

    @Test
    fun `idempotent transition to same state is valid`() {
        val current = createLifecycle(ReentryLifecycleState.ADMITTED)
        val result = guard.validateTransition(current, ReentryLifecycleState.ADMITTED)

        assertTrue(result is ReentryTransitionResult.Accepted)
        assertEquals(current, (result as ReentryTransitionResult.Accepted).updatedLifecycle)
    }

    @Test
    fun `valid decision record for COMPLETED transition is accepted`() {
        val current = createLifecycle(ReentryLifecycleState.PROCESSING)
        val record = IntelligenceReentryDecisionRecord(
            reentryIdentity = "sig1::e2",
            status = DecisionRecordStatus.CANDIDATE_PRESENTED,
            proposedState = ReentryLifecycleState.COMPLETED,
            eligibilityResult = mockk(relaxed = true),
            recordedAt = Instant.now()
        )

        val result = guard.validateDecision(current, record)

        assertTrue(result is ReentryTransitionResult.Accepted)
        assertEquals(ReentryLifecycleState.COMPLETED, (result as ReentryTransitionResult.Accepted).updatedLifecycle.currentState)
    }

    @Test
    fun `ineligible decision record is rejected`() {
        val current = createLifecycle(ReentryLifecycleState.PROCESSING)
        val record = IntelligenceReentryDecisionRecord(
            reentryIdentity = "sig1::e2",
            status = DecisionRecordStatus.INELIGIBLE_FOR_DECISION,
            proposedState = null,
            eligibilityResult = mockk(relaxed = true),
            recordedAt = Instant.now()
        )

        val result = guard.validateDecision(current, record)

        assertTrue(result is ReentryTransitionResult.Rejected)
    }

    private fun createLifecycle(state: ReentryLifecycleState) = IntelligenceReentryLifecycle(
        reentryIdentity = "sig1::e2",
        currentState = state,
        intelligenceId = "sig1",
        stateEntryId = "e2",
        admittedAt = Instant.now(),
        lastTransitionAt = Instant.now()
    )

    private fun mockk(relaxed: Boolean): IntelligenceReentryDecisionEligibilityResult = 
        IntelligenceReentryDecisionEligibilityResult(
            reentryIdentity = "sig1::e2",
            status = IntelligenceReentryDecisionEligibilityStatus.ELIGIBLE_FOR_CONSIDERATION,
            evaluatedAt = Instant.now()
        )
}
