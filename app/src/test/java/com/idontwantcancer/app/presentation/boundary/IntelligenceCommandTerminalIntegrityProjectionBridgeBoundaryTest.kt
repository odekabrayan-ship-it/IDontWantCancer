package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandTerminalIntegrityProjectionBridgeBoundaryTest {

    private val projectionAuthority = mockk<CommandConsumptionFinalityProjectionBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandTerminalIntegrityProjectionBridgeBoundary(projectionAuthority)

    @Test
    fun `routeToProjection maps terminal integrity handover request and delegates to authority`() {
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL
        )
        val request = IntelligenceCommandTerminalIntegrityProjectionHandoverRequest(finalityResult)
        
        boundary.routeToProjection(request)

        verify { projectionAuthority.projectTerminality(match { it.finalityResult == finalityResult }) }
    }
}
