package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryAuditTrailTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val guard = DefaultIntelligenceReentryTransitionGuard()
    private val ledger = DefaultIntelligenceReentryLifecycleLedger(memory, guard)

    @Test
    fun `admitEvent creates first audit record with sequence 0`() = runTest {
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

        coVerify { 
            memory.recordReentryTransition(
                any(), 
                match { it.sequenceNumber == 0 && it.previousState == null && it.newState == ReentryLifecycleState.ADMITTED }
            ) 
        }
    }

    @Test
    fun `valid transition creates subsequent audit record with correct sequence`() = runTest {
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
        coEvery { memory.getReentryAuditHistory(identity) } returns listOf(mockk()) // One existing record
        coEvery { memory.recordReentryTransition(any(), any()) } just Runs

        ledger.transitionState(identity, ReentryLifecycleState.PROCESSING, "Start")

        coVerify { 
            memory.recordReentryTransition(
                any(), 
                match { it.sequenceNumber == 1 && it.previousState == ReentryLifecycleState.ADMITTED && it.newState == ReentryLifecycleState.PROCESSING }
            ) 
        }
    }
}
