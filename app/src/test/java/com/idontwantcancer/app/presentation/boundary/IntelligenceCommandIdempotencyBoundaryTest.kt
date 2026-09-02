package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandIdempotencyStatus
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandIdempotencyBoundaryTest {

    private val boundary = DefaultIntelligenceCommandIdempotencyBoundary()

    @Test
    fun `standard UI interaction results in PROCEED status`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val result = boundary.evaluateIdempotency(interaction, "test-id")

        assertEquals(CommandIdempotencyStatus.PROCEED, result.status)
    }

    @Test
    fun `repeated check for same identity is deterministic`() {
        val interaction = IntelligenceUiInteraction.ClearSearch
        val r1 = boundary.evaluateIdempotency(interaction, "id1")
        val r2 = boundary.evaluateIdempotency(interaction, "id1")

        assertEquals(r1.status, r2.status)
        assertEquals(r1.commandIdentity, r2.commandIdentity)
    }
}
