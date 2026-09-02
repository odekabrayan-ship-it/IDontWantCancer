package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceReentryLifecycleQueryBoundaryTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val recoveryBoundary = mockk<IntelligenceReentryRecoveryBoundary>()
    private val queryBoundary = DefaultIntelligenceReentryLifecycleQueryBoundary(memory, recoveryBoundary)

    @Test
    fun `getVerifiedLifecycle delegates to recoveryBoundary`() = runTest {
        val identity = "sig1::e2"
        val expectedResult = ReentryRecoveryResult.Verified(
            IntelligenceReentryLifecycle(
                reentryIdentity = identity,
                currentState = ReentryLifecycleState.ADMITTED,
                intelligenceId = "sig1",
                stateEntryId = "e2",
                admittedAt = Instant.now(),
                lastTransitionAt = Instant.now()
            )
        )
        
        coEvery { recoveryBoundary.getVerifiedReentry(identity) } returns expectedResult

        val result = queryBoundary.getVerifiedLifecycle(identity)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getVerifiedHistory returns verified history when recovery is successful`() = runTest {
        val identity = "sig1::e2"
        val lifecycle = mockk<IntelligenceReentryLifecycle>()
        val recoveryResult = ReentryRecoveryResult.Verified(lifecycle)
        
        val history = listOf(
            IntelligenceReentryAuditEntry(identity, 1, ReentryLifecycleState.ADMITTED, ReentryLifecycleState.PROCESSING, null, Instant.now()),
            IntelligenceReentryAuditEntry(identity, 0, null, ReentryLifecycleState.ADMITTED, null, Instant.now())
        )

        coEvery { recoveryBoundary.getVerifiedReentry(identity) } returns recoveryResult
        coEvery { memory.getReentryAuditHistory(identity) } returns history

        val result = queryBoundary.getVerifiedHistory(identity)

        assertTrue(result is ReentryHistoryResult.Verified)
        val verified = result as ReentryHistoryResult.Verified
        assertEquals(2, verified.history.size)
        // Check deterministic sort by sequence number
        assertEquals(0, verified.history[0].sequenceNumber)
        assertEquals(1, verified.history[1].sequenceNumber)
    }

    @Test
    fun `getVerifiedHistory returns unverified result when recovery fails`() = runTest {
        val identity = "sig1::e2"
        val recoveryResult = ReentryRecoveryResult.Unverified(identity, null, "Integrity failed")

        coEvery { recoveryBoundary.getVerifiedReentry(identity) } returns recoveryResult

        val result = queryBoundary.getVerifiedHistory(identity)

        assertTrue(result is ReentryHistoryResult.Unverified)
        val unverified = result as ReentryHistoryResult.Unverified
        assertTrue(unverified.reason.contains("Integrity failed"))
    }
}
