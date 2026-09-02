package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceReentryConsumptionIntegrityGateTest {

    private val gate = DefaultIntelligenceReentryConsumptionIntegrityGate()

    @Test
    fun `valid consumption contract produces VALID status`() {
        val contract = IntelligenceReentryHandoffConsumptionContract(
            reentryIdentity = "sig1::e2",
            verifiedState = ReentryLifecycleState.PROCESSING,
            intelligenceId = "sig1",
            stateEntryId = "e2",
            transitionReason = "R",
            admittedAt = Instant.now(),
            lastTransitionAt = Instant.now(),
            isTerminal = false
        )

        val result = gate.validateConsumption(contract)

        assertEquals(ConsumptionValidationStatus.VALID_CONSUMPTION, result.status)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `missing identity produces INVALID status`() {
        val contract = IntelligenceReentryHandoffConsumptionContract(
            reentryIdentity = "",
            verifiedState = ReentryLifecycleState.PROCESSING,
            intelligenceId = "sig1",
            stateEntryId = "e2",
            transitionReason = "R",
            admittedAt = Instant.now(),
            lastTransitionAt = Instant.now(),
            isTerminal = false
        )

        val result = gate.validateConsumption(contract)

        assertEquals(ConsumptionValidationStatus.INVALID_CONSUMPTION, result.status)
        assertTrue(result.errors.contains(ConsumptionValidationError.MISSING_IDENTITY))
    }
}
