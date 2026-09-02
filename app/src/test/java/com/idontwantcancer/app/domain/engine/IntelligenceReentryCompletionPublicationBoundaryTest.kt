package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryCompletionPublicationBoundaryTest {

    private val boundary = DefaultIntelligenceReentryCompletionPublicationBoundary()

    @Test
    fun `completed result is successfully published to the stream`() = runTest {
        val now = Instant.now()
        val completionResult = IntelligenceReentryTransitionCompletionResult(
            reentryIdentity = "sig1::e2",
            status = ReentryTransitionCompletionStatus.COMPLETED,
            verificationResult = IntelligenceReentryPostTransitionVerificationResult(
                reentryIdentity = "sig1::e2",
                status = ReentryPostTransitionVerificationStatus.VERIFIED,
                expectedState = ReentryLifecycleState.COMPLETED,
                actualState = ReentryLifecycleState.COMPLETED,
                verifiedAt = now
            ),
            completedAt = now
        )

        boundary.publishCompletion(completionResult)

        val published = boundary.completionStream.first()
        assertEquals(completionResult, published)
    }

    @Test
    fun `publication is deterministic`() = runTest {
        val now = Instant.now()
        val result = createMockResult(now)

        boundary.publishCompletion(result)
        val published1 = boundary.completionStream.first()
        
        boundary.publishCompletion(result)
        val published2 = boundary.completionStream.first()

        assertEquals(published1, published2)
    }

    private fun createMockResult(time: Instant) = IntelligenceReentryTransitionCompletionResult(
        reentryIdentity = "sig1::e2",
        status = ReentryTransitionCompletionStatus.COMPLETED,
        verificationResult = IntelligenceReentryPostTransitionVerificationResult(
            reentryIdentity = "sig1::e2",
            status = ReentryPostTransitionVerificationStatus.VERIFIED,
            expectedState = ReentryLifecycleState.COMPLETED,
            actualState = ReentryLifecycleState.COMPLETED,
            verifiedAt = time
        ),
        completedAt = time
    )
}
