package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandFinalityTerminalIntegrityHandoverIntegrityTest {

    private val integrityBridge = mockk<IntelligenceCommandFinalityTerminalIntegrityBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandFinalityTerminalIntegrityHandoverBoundary(integrityBridge)

    @Test
    fun `handover preserves finality identity and outcome`() {
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op194",
            finality = CommandConsumptionFinality.TERMINAL,
            evaluatedAt = Instant.now()
        )
        
        val expectedProtectedResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op194",
            finality = CommandConsumptionFinality.TERMINAL,
            reason = "Protected"
        )
        
        every { integrityBridge.routeToIntegrity(any()) } returns expectedProtectedResult

        val actual = boundary.routeToIntegrity(finalityResult)

        assertEquals("op194", actual.operationId)
        assertEquals(CommandConsumptionFinality.TERMINAL, actual.finality)
        verify { 
            integrityBridge.routeToIntegrity(match { 
                it.finalityResult == finalityResult && it.finalityResult.operationId == "op194" 
            }) 
        }
    }
}
