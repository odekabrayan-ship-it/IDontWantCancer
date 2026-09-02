package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

class IntelligenceReentryHandoffBoundaryTest {

    private val isolatedBoundary = mockk<IntelligenceReentryIsolatedConsumerBoundary>()
    private val handoffBoundary = DefaultIntelligenceReentryHandoffBoundary(isolatedBoundary)

    @Test
    fun `valid isolated admitted lifecycle context is handed off`() = runTest {
        val identity = "sig1::e2"
        val contract = createMockContract(identity)
        
        coEvery { isolatedBoundary.getIsolatedContract(identity, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY) } returns contract

        val result = handoffBoundary.performHandoff(identity, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY)

        assertEquals(IntelligenceReentryHandoffStatus.HANDOFF_ACCEPTED, result.status)
        assertEquals(contract, result.contract)
    }

    @Test
    fun `non-admitted context results in handoff rejection`() = runTest {
        val identity = "sig1::e2"
        
        coEvery { isolatedBoundary.getIsolatedContract(identity, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY) } returns null

        val result = handoffBoundary.performHandoff(identity, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY)

        assertEquals(IntelligenceReentryHandoffStatus.HANDOFF_REJECTED, result.status)
        assertNull(result.contract)
    }

    private fun createMockContract(identity: String) = IntelligenceReentryHandoffConsumptionContract(
        reentryIdentity = identity,
        verifiedState = ReentryLifecycleState.PROCESSING,
        intelligenceId = "sig1",
        stateEntryId = "e2",
        transitionReason = "R",
        admittedAt = Instant.now(),
        lastTransitionAt = Instant.now(),
        isTerminal = false
    )
}
