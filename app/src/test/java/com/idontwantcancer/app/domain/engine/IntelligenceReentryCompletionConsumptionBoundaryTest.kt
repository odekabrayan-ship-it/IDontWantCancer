package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryCompletionConsumptionBoundaryTest {

    private val publicationBoundary = mockk<IntelligenceReentryCompletionPublicationBoundary>()

    @Test
    fun `published completion can be consumed through the boundary`() = runTest {
        val now = Instant.now()
        val completionResult = createMockCompletionResult(now)
        val flow = MutableSharedFlow<IntelligenceReentryTransitionCompletionResult>(replay = 1)
        flow.emit(completionResult)

        every { publicationBoundary.completionStream } returns flow
        
        val consumptionBoundary = DefaultIntelligenceReentryCompletionConsumptionBoundary(publicationBoundary)

        val consumed = consumptionBoundary.completions.first()
        assertEquals(completionResult, consumed)
    }

    private fun createMockCompletionResult(time: Instant) = IntelligenceReentryTransitionCompletionResult(
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
