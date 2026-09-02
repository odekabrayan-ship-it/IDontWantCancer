package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceReentryTransitionExecutionBoundaryTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val executionBoundary = DefaultIntelligenceReentryTransitionExecutionBoundary(memory)

    @Test
    fun `authorized transition reaches execution and creates audit record`() = runTest {
        val now = Instant.now()
        val lifecycle = createLifecycle("sig1::e2", ReentryLifecycleState.PROCESSING)
        val authorization = IntelligenceReentryTransitionAuthorization(
            updatedLifecycle = lifecycle,
            previousState = ReentryLifecycleState.ADMITTED,
            reason = "Auth test",
            authorizedAt = now
        )

        coEvery { memory.getReentryAuditHistory("sig1::e2") } returns emptyList()
        coEvery { memory.recordReentryTransition(any(), any()) } just Runs

        val result = executionBoundary.executeTransition(authorization)

        assertTrue(result is ReentryTransitionResult.Accepted)
        coVerify { 
            memory.recordReentryTransition(
                match { it.currentState == ReentryLifecycleState.PROCESSING },
                match { it.sequenceNumber == 0 && it.newState == ReentryLifecycleState.PROCESSING }
            ) 
        }
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
