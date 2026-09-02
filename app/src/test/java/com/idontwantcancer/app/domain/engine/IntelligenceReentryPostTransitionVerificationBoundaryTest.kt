package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryPostTransitionVerificationBoundaryTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val boundary = DefaultIntelligenceReentryPostTransitionVerificationBoundary(memory)

    @Test
    fun `successful authorized transition is verified`() = runTest {
        val identity = "sig1::e2"
        val lifecycle = createLifecycle(identity, ReentryLifecycleState.PROCESSING)
        val transitionResult = ReentryTransitionResult.Accepted(lifecycle)

        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns lifecycle

        val result = boundary.verifyTransition(transitionResult)

        assertEquals(ReentryPostTransitionVerificationStatus.VERIFIED, result.status)
        assertEquals(ReentryLifecycleState.PROCESSING, result.actualState)
    }

    @Test
    fun `incorrect post-transition state is detected as INCONSISTENT`() = runTest {
        val identity = "sig1::e2"
        val expectedLifecycle = createLifecycle(identity, ReentryLifecycleState.COMPLETED)
        val actualLifecycle = createLifecycle(identity, ReentryLifecycleState.PROCESSING) // Mismatch
        
        val transitionResult = ReentryTransitionResult.Accepted(expectedLifecycle)

        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns actualLifecycle

        val result = boundary.verifyTransition(transitionResult)

        assertEquals(ReentryPostTransitionVerificationStatus.INCONSISTENT, result.status)
    }

    private fun createLifecycle(id: String, state: ReentryLifecycleState) = IntelligenceReentryLifecycle(
        reentryIdentity = id,
        currentState = state,
        intelligenceId = "sig1",
        stateEntryId = "e2",
        admittedAt = Instant.now(),
        lastTransitionAt = Instant.now()
    )
}
