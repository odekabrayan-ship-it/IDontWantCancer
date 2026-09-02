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

class IntelligenceReentryIntegrityGateTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val guard = DefaultIntelligenceReentryTransitionGuard()
    private val gate = DefaultIntelligenceReentryIntegrityGate(memory, guard)

    @Test
    fun `valid lifecycle history produces INTEGRITY_CONFIRMED`() = runTest {
        val identity = "sig1::e2"
        val now = Instant.now()
        val lifecycle = createLifecycle(identity, ReentryLifecycleState.PROCESSING)
        val audit = listOf(
            createAudit(identity, 0, null, ReentryLifecycleState.ADMITTED),
            createAudit(identity, 1, ReentryLifecycleState.ADMITTED, ReentryLifecycleState.PROCESSING)
        )

        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns lifecycle
        coEvery { memory.getReentryAuditHistory(identity) } returns audit

        val result = gate.verifyIntegrity(identity)

        assertEquals(ReentryIntegrityStatus.INTEGRITY_CONFIRMED, result.status)
        assertEquals(ReentryLifecycleState.PROCESSING, result.reconstructedState)
    }

    @Test
    fun `state mismatch produces INTEGRITY_FAILED`() = runTest {
        val identity = "sig1::e2"
        val lifecycle = createLifecycle(identity, ReentryLifecycleState.COMPLETED) // Mismatch
        val audit = listOf(
            createAudit(identity, 0, null, ReentryLifecycleState.ADMITTED),
            createAudit(identity, 1, ReentryLifecycleState.ADMITTED, ReentryLifecycleState.PROCESSING)
        )

        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns lifecycle
        coEvery { memory.getReentryAuditHistory(identity) } returns audit

        val result = gate.verifyIntegrity(identity)

        assertEquals(ReentryIntegrityStatus.INTEGRITY_FAILED, result.status)
        assertTrue(result.failureReasons.contains(ReentryIntegrityFailureReason.STATE_MISMATCH))
    }

    @Test
    fun `invalid transition history produces INTEGRITY_FAILED`() = runTest {
        val identity = "sig1::e2"
        val audit = listOf(
            createAudit(identity, 0, null, ReentryLifecycleState.ADMITTED),
            createAudit(identity, 1, ReentryLifecycleState.COMPLETED, ReentryLifecycleState.PROCESSING) // Broken link
        )

        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns null
        coEvery { memory.getReentryAuditHistory(identity) } returns audit

        val result = gate.verifyIntegrity(identity)

        assertEquals(ReentryIntegrityStatus.INTEGRITY_FAILED, result.status)
        assertTrue(result.failureReasons.contains(ReentryIntegrityFailureReason.INVALID_TRANSITION_HISTORY))
    }

    @Test
    fun `ambiguous ordering produces INTEGRITY_FAILED`() = runTest {
        val identity = "sig1::e2"
        val audit = listOf(
            createAudit(identity, 0, null, ReentryLifecycleState.ADMITTED),
            createAudit(identity, 2, ReentryLifecycleState.ADMITTED, ReentryLifecycleState.PROCESSING) // Gap in sequence
        )

        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns null
        coEvery { memory.getReentryAuditHistory(identity) } returns audit

        val result = gate.verifyIntegrity(identity)

        assertTrue(result.failureReasons.contains(ReentryIntegrityFailureReason.AMBIGUOUS_ORDER))
    }

    private fun createLifecycle(identity: String, state: ReentryLifecycleState) = IntelligenceReentryLifecycle(
        reentryIdentity = identity,
        currentState = state,
        intelligenceId = "sig1",
        stateEntryId = "e2",
        admittedAt = Instant.now(),
        lastTransitionAt = Instant.now()
    )

    private fun createAudit(
        identity: String,
        seq: Int,
        prev: ReentryLifecycleState?,
        next: ReentryLifecycleState
    ) = IntelligenceReentryAuditEntry(
        reentryIdentity = identity,
        sequenceNumber = seq,
        previousState = prev,
        newState = next,
        transitionReason = "R",
        timestamp = Instant.now()
    )
}
