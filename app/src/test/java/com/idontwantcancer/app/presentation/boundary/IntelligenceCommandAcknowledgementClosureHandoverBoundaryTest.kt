package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandAcknowledgementClosureHandoverBoundaryTest {

    private val closureAuthority = mockk<IntelligenceCommandAcknowledgementClosureBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandAcknowledgementClosureHandoverBoundary(closureAuthority)

    @Test
    fun `routeToClosure maps acknowledgement outcome to handover request and delegates to bridge`() = runTest {
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
        
        coEvery { closureAuthority.routeToClosure(any()) } returns closureResult

        val actual = boundary.routeToClosure(request)

        assertEquals(closureResult, actual)
        coVerify { closureAuthority.routeToClosure(match { it.result == result && it.acknowledgementResult == acknowledgement }) }
    }
}
