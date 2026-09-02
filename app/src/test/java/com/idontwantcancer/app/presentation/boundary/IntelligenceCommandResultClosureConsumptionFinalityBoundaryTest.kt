package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultClosureConsumptionFinalityBoundaryTest {

    private val boundary = DefaultIntelligenceCommandResultClosureConsumptionFinalityBoundary()

    @Test
    fun `CLOSED closure results in TERMINAL finality`() {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )
        val ack = IntelligenceCommandResultClosureConsumptionAcknowledgementResult(
            operationId = "op1",
            acknowledgedAt = Instant.now()
        )

        val result = boundary.evaluateFinality(closure, ack)

        assertEquals(CommandConsumptionFinality.TERMINAL, result.finality)
    }

    @Test
    fun `OPEN closure results in NON_TERMINAL finality`() {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op2",
            status = CommandResultClosureStatus.OPEN,
            evaluatedAt = Instant.now()
        )
        val ack = IntelligenceCommandResultClosureConsumptionAcknowledgementResult(
            operationId = "op2",
            acknowledgedAt = Instant.now()
        )

        val result = boundary.evaluateFinality(closure, ack)

        assertEquals(CommandConsumptionFinality.NON_TERMINAL, result.finality)
    }

    @Test
    fun `finality evaluation is deterministic`() {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op3",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )
        val ack = IntelligenceCommandResultClosureConsumptionAcknowledgementResult(
            operationId = "op3",
            acknowledgedAt = Instant.now()
        )

        val r1 = boundary.evaluateFinality(closure, ack)
        val r2 = boundary.evaluateFinality(closure, ack)

        assertEquals(r1.finality, r2.finality)
        assertEquals(r1.operationId, r2.operationId)
    }
}
