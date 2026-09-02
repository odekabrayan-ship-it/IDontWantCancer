package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandFinalityTerminalIntegrityBridgeBoundaryTest {

    private val integrityAuthority = mockk<IntelligenceCommandTerminalStateIntegrityBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandFinalityTerminalIntegrityBridgeBoundary(integrityAuthority)

    @Test
    fun `routeToIntegrity maps finality handover request and delegates to authoritative gate`() {
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL
        )
        val request = IntelligenceCommandFinalityTerminalIntegrityHandoverRequest(finalityResult)
        
        every { integrityAuthority.protectFinality(any()) } returns finalityResult

        val actual = boundary.routeToIntegrity(request)

        assertEquals(finalityResult, actual)
        verify { integrityAuthority.protectFinality(match { it.finalityResult == finalityResult }) }
    }
}
