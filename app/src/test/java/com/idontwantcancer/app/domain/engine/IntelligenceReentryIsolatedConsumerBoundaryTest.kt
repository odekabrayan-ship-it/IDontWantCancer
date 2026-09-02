package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

class IntelligenceReentryIsolatedConsumerBoundaryTest {

    private val queryBoundary = mockk<IntelligenceReentryLifecycleQueryBoundary>()
    private val admissionBoundary = mockk<IntelligenceReentryConsumerAdmissionBoundary>()
    private val boundary = DefaultIntelligenceReentryIsolatedConsumerBoundary(queryBoundary, admissionBoundary)

    @Test
    fun `admitted lifecycle reaches the consumer`() = runTest {
        val identity = "sig1::e2"
        val contract = createMockContract(identity)
        
        coEvery { queryBoundary.getConsumptionContract(identity) } returns contract
        coEvery { admissionBoundary.evaluateAdmission(contract, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY) } returns 
            IntelligenceConsumerAdmissionResult.Admitted(contract, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY, Instant.now())

        val result = boundary.getIsolatedContract(identity, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY)

        assertEquals(contract, result)
    }

    @Test
    fun `rejected lifecycle is isolated from the consumer`() = runTest {
        val identity = "sig1::e2"
        val contract = createMockContract(identity)
        
        coEvery { queryBoundary.getConsumptionContract(identity) } returns contract
        coEvery { admissionBoundary.evaluateAdmission(contract, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY) } returns 
            IntelligenceConsumerAdmissionResult.Rejected(identity, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY, ReentryAdmissionRejectionReason.INVALID_CONSUMPTION, Instant.now())

        val result = boundary.getIsolatedContract(identity, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY)

        assertNull(result)
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
