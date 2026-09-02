package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceCommandTerminalStateIntegrityBoundaryTest {

    private val boundary = DefaultIntelligenceCommandTerminalStateIntegrityBoundary()

    @Test
    fun `verifyIntegrity returns true for unknown command`() {
        assertTrue(boundary.verifyIntegrity("new_op"))
    }

    @Test
    fun `protectTerminality records result and subsequent verifyIntegrity returns false`() {
        val identity = "op1"
        val result = IntelligenceApplicationCommandResult.Success(operationId = identity)
        
        val protected = boundary.protectTerminality(identity, result)
        
        assertEquals(result, protected)
        assertFalse(boundary.verifyIntegrity(identity))
    }

    @Test
    fun `protectTerminality prevents mutation of already recorded result`() {
        val identity = "op2"
        val r1 = IntelligenceApplicationCommandResult.Success(operationId = identity)
        val r2 = IntelligenceApplicationCommandResult.Failure("Late failure", identity)
        
        val protected1 = boundary.protectTerminality(identity, r1)
        val protected2 = boundary.protectTerminality(identity, r2)
        
        assertEquals(r1, protected1)
        assertEquals(r1, protected2) // r1 was first, terminal integrity preserved
    }

    @Test
    fun `protectFinality records finality result and prevents mutation`() {
        val identity = "op3"
        val f1 = IntelligenceCommandConsumptionFinalityResult(
            operationId = identity,
            finality = CommandConsumptionFinality.TERMINAL
        )
        val f2 = IntelligenceCommandConsumptionFinalityResult(
            operationId = identity,
            finality = CommandConsumptionFinality.NON_TERMINAL
        )
        
        val protected1 = boundary.protectFinality(IntelligenceCommandTerminalIntegrityRequest(f1))
        val protected2 = boundary.protectFinality(IntelligenceCommandTerminalIntegrityRequest(f2))
        
        assertEquals(f1, protected1)
        assertEquals(f1, protected2) // f1 was first
    }
}
