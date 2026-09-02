package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceReentryLifecycleLedgerTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val guard = DefaultIntelligenceReentryTransitionGuard()
    private val ledger = DefaultIntelligenceReentryLifecycleLedger(memory, guard)

    @Test
    fun `admitEvent creates initial lifecycle record`() = runTest {
        val dedupResult = IntelligenceReentryDeduplicationResult(
            reentryIdentity = "sig1::e2",
            status = DeduplicationStatus.NEW_REENTRY,
            intelligenceId = "sig1",
            stateEntryId = "e2",
            reason = "R",
            evaluatedAt = Instant.now()
        )
        
        coEvery { memory.recordReentryTransition(any(), any()) } just Runs

        val lifecycle = ledger.admitEvent(dedupResult)

        assertEquals(ReentryLifecycleState.ADMITTED, lifecycle.currentState)
        assertEquals("sig1::e2", lifecycle.reentryIdentity)
    }

    @Test
    fun `valid transition succeeds`() = runTest {
        val identity = "sig1::e2"
        val existing = IntelligenceReentryLifecycle(
            reentryIdentity = identity,
            currentState = ReentryLifecycleState.ADMITTED,
            intelligenceId = "sig1",
            stateEntryId = "e2",
            admittedAt = Instant.now(),
            lastTransitionAt = Instant.now()
        )

        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns existing
        coEvery { memory.getReentryAuditHistory(identity) } returns emptyList()
        coEvery { memory.recordReentryTransition(any(), any()) } just Runs

        val result = ledger.transitionState(identity, ReentryLifecycleState.PROCESSING, "Starting eval")

        assertTrue(result is ReentryTransitionResult.Accepted)
        val updated = (result as ReentryTransitionResult.Accepted).updatedLifecycle
        assertEquals(ReentryLifecycleState.PROCESSING, updated.currentState)
        assertEquals("Starting eval", updated.transitionReason)
    }

    @Test
    fun `transition from terminal state fails via guard`() = runTest {
        val identity = "sig1::e2"
        val existing = IntelligenceReentryLifecycle(
            reentryIdentity = identity,
            currentState = ReentryLifecycleState.COMPLETED,
            intelligenceId = "sig1",
            stateEntryId = "e2",
            admittedAt = Instant.now(),
            lastTransitionAt = Instant.now()
        )

        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns existing

        val result = ledger.transitionState(identity, ReentryLifecycleState.PROCESSING)
        
        assertTrue(result is ReentryTransitionResult.Rejected)
        assertEquals(ReentryTransitionRejectionReason.TERMINAL_STATE, (result as ReentryTransitionResult.Rejected).reason)
    }
}
