package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

class IntelligenceReentryHandoffConsumptionContractTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val recoveryBoundary = mockk<IntelligenceReentryRecoveryBoundary>()
    private val queryBoundary = DefaultIntelligenceReentryLifecycleQueryBoundary(memory, recoveryBoundary)

    @Test
    fun `verified Step 84 state produces a valid Step 90 contract`() = runTest {
        val identity = "sig1::e2"
        val now = Instant.now()
        val lifecycle = IntelligenceReentryLifecycle(
            reentryIdentity = identity,
            currentState = ReentryLifecycleState.PROCESSING,
            intelligenceId = "sig1",
            stateEntryId = "e2",
            admittedAt = now,
            lastTransitionAt = now,
            transitionReason = "New data"
        )
        val recoveryResult = ReentryRecoveryResult.Verified(lifecycle)

        coEvery { recoveryBoundary.getVerifiedReentry(identity) } returns recoveryResult

        val contract = queryBoundary.getConsumptionContract(identity)

        assertNotNull(contract)
        assertEquals(identity, contract?.reentryIdentity)
        assertEquals(ReentryLifecycleState.PROCESSING, contract?.verifiedState)
        assertEquals("New data", contract?.transitionReason)
        assertEquals("e2", contract?.stateEntryId)
    }

    @Test
    fun `unverified state cannot produce a verified Step 90 contract`() = runTest {
        val identity = "sig1::e2"
        val recoveryResult = ReentryRecoveryResult.Unverified(identity, null, "Integrity failed")

        coEvery { recoveryBoundary.getVerifiedReentry(identity) } returns recoveryResult

        val contract = queryBoundary.getConsumptionContract(identity)

        assertNull(contract)
    }
}
