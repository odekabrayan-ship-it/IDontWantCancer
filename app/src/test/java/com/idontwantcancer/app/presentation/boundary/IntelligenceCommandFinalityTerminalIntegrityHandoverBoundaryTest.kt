package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandFinalityTerminalIntegrityHandoverBoundaryTest {

    private val integrityBridge = mockk<IntelligenceCommandFinalityTerminalIntegrityBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandFinalityTerminalIntegrityHandoverBoundary(integrityBridge)

    @Test
    fun `routeToIntegrity maps finality outcome to handover request and delegates to integrity authority`() {
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL
        )
        
        val expectedResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL
        )
        
        every { integrityBridge.routeToIntegrity(any()) } returns expectedResult

        val actual = boundary.routeToIntegrity(finalityResult)

        assertEquals(expectedResult, actual)
        verify { integrityBridge.routeToIntegrity(match { it.finalityResult == finalityResult }) }
    }
}
