package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandAcknowledgementClosureBridgeBoundaryTest {

    private val closureAuthority = mockk<IntelligenceCommandResultAcknowledgementClosureBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandAcknowledgementClosureBridgeBoundary(closureAuthority)

    @Test
    fun `routeToClosure maps acknowledgement handover request and delegates to authority`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val acknowledgement = IntelligenceCommandAcknowledgementResult(
            operationId = "op1",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED
        )
        val request = IntelligenceCommandAcknowledgementClosureHandoverRequest(result, acknowledgement)
        
        val closureResult = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED
        )
        
        coEvery { closureAuthority.evaluateClosure(any()) } returns closureResult

        val actual = boundary.routeToClosure(request)

        assertEquals(closureResult, actual)
        coVerify { closureAuthority.evaluateClosure(match { it.result == result && it.acknowledgementResult == acknowledgement }) }
    }
}
