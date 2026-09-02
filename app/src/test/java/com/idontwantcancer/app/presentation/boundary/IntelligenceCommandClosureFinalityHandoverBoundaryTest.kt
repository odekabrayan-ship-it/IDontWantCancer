package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandClosureFinalityHandoverBoundaryTest {

    private val finalityBridge = mockk<IntelligenceCommandClosureFinalityBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandClosureFinalityHandoverBoundary(finalityBridge)

    @Test
    fun `routeToFinality maps closure result to handover request and delegates to bridge`() {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED
        )
        
        val expectedResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL
        )
        val request = IntelligenceCommandClosureFinalityHandoverRequest(closure)
        every { finalityBridge.routeToFinality(any()) } returns expectedResult

        val actual = boundary.routeToFinality(request)

        assertEquals(expectedResult, actual)
        verify { finalityBridge.routeToFinality(match { it.closure == closure }) }
    }
}
