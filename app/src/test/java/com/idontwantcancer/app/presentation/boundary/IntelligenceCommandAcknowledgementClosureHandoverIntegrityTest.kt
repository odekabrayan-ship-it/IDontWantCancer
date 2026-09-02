package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandAcknowledgementClosureHandoverIntegrityTest {

    private val closureBridge = mockk<IntelligenceCommandAcknowledgementClosureBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandAcknowledgementClosureHandoverBoundary(closureBridge)

    @Test
    fun `handover preserves acknowledgement identity and outcome`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op202")
        val acknowledgement = IntelligenceCommandAcknowledgementResult(
            operationId = "op202",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED
        )
        val request = IntelligenceCommandAcknowledgementClosureHandoverRequest(result, acknowledgement)
        
        val expectedResult = IntelligenceCommandClosureResult(
            operationId = "op202",
            status = CommandResultClosureStatus.CLOSED
        )
        
        coEvery { closureBridge.routeToClosure(any()) } returns expectedResult

        val actual = boundary.routeToClosure(request)

        assertEquals("op202", actual.operationId)
        assertEquals(CommandResultClosureStatus.CLOSED, actual.status)
        coVerify { 
            closureBridge.routeToClosure(match { 
                it.result == result && it.acknowledgementResult == acknowledgement 
            }) 
        }
    }
}
