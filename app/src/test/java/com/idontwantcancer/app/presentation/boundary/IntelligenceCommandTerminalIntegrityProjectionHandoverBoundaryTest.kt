package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandTerminalIntegrityProjectionHandoverBoundaryTest {

    private val projectionBridge = mockk<IntelligenceCommandTerminalIntegrityProjectionBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandTerminalIntegrityProjectionHandoverBoundary(projectionBridge)

    @Test
    fun `routeToProjection maps terminal integrity result to handover request and delegates to bridge`() {
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL
        )
        
        val request = IntelligenceCommandTerminalIntegrityProjectionHandoverRequest(finalityResult)
        boundary.routeToProjection(request)

        verify { projectionBridge.routeToProjection(match { it.finalityResult == finalityResult }) }
    }
}
