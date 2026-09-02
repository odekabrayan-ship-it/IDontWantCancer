package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandClosureFinalityBridgeBoundaryTest {

    private val finalityAuthority = mockk<IntelligenceCommandResultClosureFinalityBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandClosureFinalityBridgeBoundary(finalityAuthority)

    @Test
    fun `routeToFinality maps handover request to internal finality request and delegates`() {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED
        )
        val request = IntelligenceCommandClosureFinalityHandoverRequest(closure)
        
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL
        )
        
        every { finalityAuthority.evaluateFinality(any()) } returns finalityResult

        val actual = boundary.routeToFinality(request)

        assertEquals(finalityResult, actual)
        verify { finalityAuthority.evaluateFinality(match { it.closure == closure }) }
    }
}
