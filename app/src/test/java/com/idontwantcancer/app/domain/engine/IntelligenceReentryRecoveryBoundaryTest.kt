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

class IntelligenceReentryRecoveryBoundaryTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val integrityGate = mockk<IntelligenceReentryIntegrityGate>()
    private val boundary = DefaultIntelligenceReentryRecoveryBoundary(memory, integrityGate)

    @Test
    fun `integrity-confirmed recovery produces VERIFIED result`() = runTest {
        val identity = "sig1::e2"
        val integrityResult = IntelligenceReentryIntegrityResult(
            reentryIdentity = identity,
            status = ReentryIntegrityStatus.INTEGRITY_CONFIRMED,
            storedState = ReentryLifecycleState.PROCESSING,
            reconstructedState = ReentryLifecycleState.PROCESSING,
            failureReasons = emptyList(),
            verifiedAt = Instant.now()
        )
        val lifecycle = IntelligenceReentryLifecycle(
            reentryIdentity = identity,
            currentState = ReentryLifecycleState.PROCESSING,
            intelligenceId = "sig1",
            stateEntryId = "e2",
            admittedAt = Instant.now(),
            lastTransitionAt = Instant.now()
        )

        coEvery { integrityGate.verifyIntegrity(identity) } returns integrityResult
        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns lifecycle

        val result = boundary.getVerifiedReentry(identity)

        assertTrue(result is ReentryRecoveryResult.Verified)
        assertEquals(ReentryLifecycleState.PROCESSING, (result as ReentryRecoveryResult.Verified).lifecycle.currentState)
    }

    @Test
    fun `integrity failure produces UNVERIFIED result`() = runTest {
        val identity = "sig1::e2"
        val integrityResult = IntelligenceReentryIntegrityResult(
            reentryIdentity = identity,
            status = ReentryIntegrityStatus.INTEGRITY_FAILED,
            storedState = ReentryLifecycleState.COMPLETED,
            reconstructedState = ReentryLifecycleState.PROCESSING,
            failureReasons = listOf(ReentryIntegrityFailureReason.STATE_MISMATCH),
            verifiedAt = Instant.now()
        )

        coEvery { integrityGate.verifyIntegrity(identity) } returns integrityResult

        val result = boundary.getVerifiedReentry(identity)

        assertTrue(result is ReentryRecoveryResult.Unverified)
        val unverified = result as ReentryRecoveryResult.Unverified
        assertEquals(identity, unverified.identity)
        assertTrue(unverified.reason.contains("integrity could not be confirmed"))
    }

    @Test
    fun `missing ledger record for verified identity produces UNVERIFIED result`() = runTest {
        val identity = "sig1::e2"
        val integrityResult = IntelligenceReentryIntegrityResult(
            reentryIdentity = identity,
            status = ReentryIntegrityStatus.INTEGRITY_CONFIRMED,
            storedState = ReentryLifecycleState.PROCESSING,
            reconstructedState = ReentryLifecycleState.PROCESSING,
            failureReasons = emptyList(),
            verifiedAt = Instant.now()
        )

        coEvery { integrityGate.verifyIntegrity(identity) } returns integrityResult
        coEvery { memory.getReentryLifecycleByIdentity(identity) } returns null // Unexpectedly missing

        val result = boundary.getVerifiedReentry(identity)

        assertTrue(result is ReentryRecoveryResult.Unverified)
        val unverified = result as ReentryRecoveryResult.Unverified
        assertTrue(unverified.reason.contains("Ledger record missing"))
    }
}
