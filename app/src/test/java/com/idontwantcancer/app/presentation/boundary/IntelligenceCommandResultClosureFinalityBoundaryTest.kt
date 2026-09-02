package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultClosureFinalityBoundaryTest {

    private val boundary = DefaultIntelligenceCommandResultClosureConsumptionFinalityBoundary()

    @Test
    fun `CLOSED result established as TERMINAL finality`() {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )
        val request = IntelligenceCommandFinalityRequest(closure)

        val finality = boundary.evaluateFinality(request)

        assertEquals(CommandConsumptionFinality.TERMINAL, finality.finality)
        assertEquals("op1", finality.operationId)
    }

    @Test
    fun `OPEN result established as NON_TERMINAL finality`() {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op2",
            status = CommandResultClosureStatus.OPEN,
            evaluatedAt = Instant.now()
        )
        val request = IntelligenceCommandFinalityRequest(closure)

        val finality = boundary.evaluateFinality(request)

        assertEquals(CommandConsumptionFinality.NON_TERMINAL, finality.finality)
    }
}
