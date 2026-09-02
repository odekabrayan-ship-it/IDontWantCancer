package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceReentryConsumerAdmissionBoundaryTest {

    private val integrityGate = mockk<IntelligenceReentryConsumptionIntegrityGate>()
    private val boundary = DefaultIntelligenceReentryConsumerAdmissionBoundary(integrityGate)

    @Test
    fun `valid consumption contract + authorized consumer = ADMITTED`() {
        val contract = createMockContract("sig1::e2")
        val validationResult = IntelligenceReentryConsumptionValidationResult(
            reentryIdentity = "sig1::e2",
            status = ConsumptionValidationStatus.VALID_CONSUMPTION,
            errors = emptyList(),
            validatedAt = Instant.now()
        )

        every { integrityGate.validateConsumption(contract) } returns validationResult

        val result = boundary.evaluateAdmission(contract, IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY)

        assertTrue(result is IntelligenceConsumerAdmissionResult.Admitted)
        assertEquals(IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY, (result as IntelligenceConsumerAdmissionResult.Admitted).consumer)
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
