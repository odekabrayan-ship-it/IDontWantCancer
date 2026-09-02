package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultClosureConsumptionFinalityExecutionResultBoundaryTest {

    private val resultAuthority = mockk<IntelligenceCommandResultBoundary>(relaxed = true)
    private val failureBoundary = mockk<IntelligenceCommandFailureBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultClosureConsumptionFinalityExecutionResultBoundary(resultAuthority, failureBoundary)

    @Test
    fun `Success outcome establishes authoritative Success result`() {
        val outcome = IntelligenceCommandExecutionOutcome.Success("op1")
        val expected = IntelligenceApplicationCommandResult.Success("op1", Instant.now())
        every { resultAuthority.success("op1") } returns expected

        val result = boundary.establishResult(outcome)

        assertEquals(expected, result)
        verify { resultAuthority.success("op1") }
    }

    @Test
    fun `Failure outcome establishes authoritative Failure result via failure boundary`() {
        val exception = Exception("Technical error")
        val outcome = IntelligenceCommandExecutionOutcome.Failure(exception, "op2")
        val expected = IntelligenceApplicationCommandResult.Failure("Technical error", "op2", Instant.now())
        
        every { failureBoundary.mapFailure(exception, "op2") } returns expected

        val result = boundary.establishResult(outcome)

        assertEquals(expected, result)
        verify { failureBoundary.mapFailure(exception, "op2") }
    }

    @Test
    fun `Rejected outcome establishes authoritative Rejected result`() {
        val outcome = IntelligenceCommandExecutionOutcome.Rejected("Domain refusal", "op3")
        val expected = IntelligenceApplicationCommandResult.Rejected("Domain refusal", "op3", Instant.now())
        every { resultAuthority.rejected("Domain refusal", "op3") } returns expected

        val result = boundary.establishResult(outcome)

        assertEquals(expected, result)
        verify { resultAuthority.rejected("Domain refusal", "op3") }
    }

    @Test
    fun `Cancelled outcome establishes authoritative Cancelled result`() {
        val outcome = IntelligenceCommandExecutionOutcome.Cancelled("op4")
        val expected = IntelligenceApplicationCommandResult.Cancelled("op4", Instant.now())
        every { resultAuthority.cancelled("op4") } returns expected

        val result = boundary.establishResult(outcome)

        assertEquals(expected, result)
        verify { resultAuthority.cancelled("op4") }
    }

    @Test
    fun `TimedOut outcome establishes authoritative TimedOut result`() {
        val outcome = IntelligenceCommandExecutionOutcome.TimedOut("op5")
        val expected = IntelligenceApplicationCommandResult.TimedOut("op5", Instant.now())
        every { resultAuthority.timedOut("op5") } returns expected

        val result = boundary.establishResult(outcome)

        assertEquals(expected, result)
        verify { resultAuthority.timedOut("op5") }
    }
}
