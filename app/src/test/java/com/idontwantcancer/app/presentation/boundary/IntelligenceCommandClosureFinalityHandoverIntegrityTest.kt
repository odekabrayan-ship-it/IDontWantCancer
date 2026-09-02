package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandClosureFinalityHandoverIntegrityTest {

    private val finalityBridge = mockk<IntelligenceCommandClosureFinalityBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandClosureFinalityHandoverBoundary(finalityBridge)

    @Test
    fun `handover preserves closure identity and outcome`() {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op193",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )
        val request = IntelligenceCommandClosureFinalityHandoverRequest(closure)
        
        val expectedResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op193",
            finality = CommandConsumptionFinality.TERMINAL
        )
        
        every { finalityBridge.routeToFinality(any()) } returns expectedResult

        val actual = boundary.routeToFinality(request)

        assertEquals("op193", actual.operationId)
        assertEquals(CommandConsumptionFinality.TERMINAL, actual.finality)
        verify { 
            finalityBridge.routeToFinality(match { 
                it.closure == closure && it.closure.operationId == "op193" 
            }) 
        }
    }
}
