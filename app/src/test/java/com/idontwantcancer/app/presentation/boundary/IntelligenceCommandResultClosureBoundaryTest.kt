package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultClosureBoundaryTest {

    private val dispositionBoundary = mockk<IntelligenceCommandResultDispositionBoundary>()
    private val boundary = DefaultIntelligenceCommandResultClosureBoundary(dispositionBoundary)

    @Test
    fun `ACCEPTED disposition results in CLOSED status`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        
        coEvery { dispositionBoundary.evaluateDisposition(result) } returns IntelligenceCommandDispositionResult(
            operationId = "op1",
            disposition = CommandResultDisposition.ACCEPTED
        )

        val closureResult = boundary.evaluateClosure(result)

        assertEquals(CommandResultClosureStatus.CLOSED, closureResult.status)
    }

    @Test
    fun `REJECTED disposition results in CLOSED status`() = runTest {
        val result = IntelligenceApplicationCommandResult.Failure("Error", "op2")
        
        coEvery { dispositionBoundary.evaluateDisposition(result) } returns IntelligenceCommandDispositionResult(
            operationId = "op2",
            disposition = CommandResultDisposition.REJECTED
        )

        val closureResult = boundary.evaluateClosure(result)

        assertEquals(CommandResultClosureStatus.CLOSED, closureResult.status)
    }

    @Test
    fun `ACKNOWLEDGED disposition results in OPEN status`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op3")
        
        coEvery { dispositionBoundary.evaluateDisposition(result) } returns IntelligenceCommandDispositionResult(
            operationId = "op3",
            disposition = CommandResultDisposition.ACKNOWLEDGED
        )

        val closureResult = boundary.evaluateClosure(result)

        assertEquals(CommandResultClosureStatus.OPEN, closureResult.status)
    }

    @Test
    fun `UNRESOLVED disposition results in OPEN status`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op4")
        
        coEvery { dispositionBoundary.evaluateDisposition(result) } returns IntelligenceCommandDispositionResult(
            operationId = "op4",
            disposition = CommandResultDisposition.UNRESOLVED
        )

        val closureResult = boundary.evaluateClosure(result)

        assertEquals(CommandResultClosureStatus.OPEN, closureResult.status)
    }

    @Test
    fun `closure request delegates to result-based evaluation`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op5")
        val acknowledgement = IntelligenceCommandAcknowledgementResult(
            operationId = "op5",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED
        )
        val request = IntelligenceCommandAcknowledgementClosureHandoverRequest(result, acknowledgement)

        coEvery { dispositionBoundary.evaluateDisposition(result) } returns IntelligenceCommandDispositionResult(
            operationId = "op5",
            disposition = CommandResultDisposition.ACCEPTED
        )

        val closureResult = boundary.evaluateClosure(request)

        assertEquals(CommandResultClosureStatus.CLOSED, closureResult.status)
    }
}
