package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryTransitionCompletionBoundaryTest {

    private val boundary = DefaultIntelligenceReentryTransitionCompletionBoundary()

    @Test
    fun `verified transition produces COMPLETED status`() {
        val verification = IntelligenceReentryPostTransitionVerificationResult(
            reentryIdentity = "sig1::e2",
            status = ReentryPostTransitionVerificationStatus.VERIFIED,
            expectedState = ReentryLifecycleState.COMPLETED,
            actualState = ReentryLifecycleState.COMPLETED,
            verifiedAt = Instant.now()
        )

        val result = boundary.completeTransition(verification)

        assertEquals(ReentryTransitionCompletionStatus.COMPLETED, result.status)
        assertEquals(verification, result.verificationResult)
    }

    @Test
    fun `inconsistent transition produces NOT_COMPLETED status`() {
        val verification = IntelligenceReentryPostTransitionVerificationResult(
            reentryIdentity = "sig1::e2",
            status = ReentryPostTransitionVerificationStatus.INCONSISTENT,
            expectedState = ReentryLifecycleState.COMPLETED,
            actualState = ReentryLifecycleState.PROCESSING,
            verifiedAt = Instant.now()
        )

        val result = boundary.completeTransition(verification)

        assertEquals(ReentryTransitionCompletionStatus.NOT_COMPLETED, result.status)
    }

    @Test
    fun `completion is deterministic`() {
        val verification = IntelligenceReentryPostTransitionVerificationResult(
            reentryIdentity = "sig1::e2",
            status = ReentryPostTransitionVerificationStatus.VERIFIED,
            expectedState = ReentryLifecycleState.COMPLETED,
            actualState = ReentryLifecycleState.COMPLETED,
            verifiedAt = Instant.now()
        )

        val result1 = boundary.completeTransition(verification)
        val result2 = boundary.completeTransition(verification)

        assertEquals(result1.status, result2.status)
        assertEquals(result1.reentryIdentity, result2.reentryIdentity)
    }
}
