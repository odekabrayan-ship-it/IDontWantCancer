package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultAcknowledgementClosureBoundaryTest {

    private val dispositionBoundary = mockk<IntelligenceCommandResultDispositionBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultClosureBoundary(dispositionBoundary)

    @Test
    fun `closure is evaluated after establishing acknowledgement handover`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val acknowledgement = IntelligenceCommandAcknowledgementResult(
            operationId = "op1",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED,
            acknowledgedAt = Instant.now()
        )
        val request = IntelligenceCommandAcknowledgementClosureHandoverRequest(result, acknowledgement)

        coEvery { dispositionBoundary.evaluateDisposition(result) } returns IntelligenceCommandDispositionResult(
            operationId = "op1",
            disposition = CommandResultDisposition.ACCEPTED,
            evaluatedAt = Instant.now()
        )

        val closure = boundary.evaluateClosure(request)

        assertEquals(CommandResultClosureStatus.CLOSED, closure.status)
        assertEquals("op1", closure.operationId)
    }

    @Test
    fun `unresolved disposition remains OPEN`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op2")
        val acknowledgement = IntelligenceCommandAcknowledgementResult(
            operationId = "op2",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED,
            acknowledgedAt = Instant.now()
        )
        val request = IntelligenceCommandAcknowledgementClosureHandoverRequest(result, acknowledgement)

        coEvery { dispositionBoundary.evaluateDisposition(result) } returns IntelligenceCommandDispositionResult(
            operationId = "op2",
            disposition = CommandResultDisposition.UNRESOLVED,
            evaluatedAt = Instant.now()
        )

        val closure = boundary.evaluateClosure(request)

        assertEquals(CommandResultClosureStatus.OPEN, closure.status)
    }
}
