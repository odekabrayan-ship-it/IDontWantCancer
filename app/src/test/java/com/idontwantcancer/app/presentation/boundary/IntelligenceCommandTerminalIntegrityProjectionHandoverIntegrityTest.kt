package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test
import java.time.Instant

class IntelligenceCommandTerminalIntegrityProjectionHandoverIntegrityTest {

    private val projectionBridge = mockk<IntelligenceCommandTerminalIntegrityProjectionBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandTerminalIntegrityProjectionHandoverBoundary(projectionBridge)

    @Test
    fun `handover preserves terminal integrity identity and outcome`() {
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op195",
            finality = CommandConsumptionFinality.TERMINAL,
            evaluatedAt = Instant.now()
        )
        
        val request = IntelligenceCommandTerminalIntegrityProjectionHandoverRequest(finalityResult)
        boundary.routeToProjection(request)

        verify { 
            projectionBridge.routeToProjection(match { 
                it.finalityResult == finalityResult && it.finalityResult.operationId == "op195" 
            }) 
        }
    }
}
