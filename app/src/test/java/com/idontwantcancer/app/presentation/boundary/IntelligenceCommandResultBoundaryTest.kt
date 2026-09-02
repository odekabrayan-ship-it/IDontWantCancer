package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IntelligenceCommandResultBoundaryTest {

    private val boundary = DefaultIntelligenceCommandResultBoundary()

    @Test
    fun `success creates a Success result model`() {
        val result = boundary.success("op1")

        assertTrue(result is IntelligenceApplicationCommandResult.Success)
        assertEquals("op1", result.operationId)
    }

    @Test
    fun `failure creates a Failure result model`() {
        val result = boundary.failure("error", "op1")

        assertTrue(result is IntelligenceApplicationCommandResult.Failure)
        assertEquals("error", (result as IntelligenceApplicationCommandResult.Failure).reason)
        assertEquals("op1", result.operationId)
    }

    @Test
    fun `rejected creates a Rejected result model`() {
        val result = boundary.rejected("no permission", "op1")

        assertTrue(result is IntelligenceApplicationCommandResult.Rejected)
        assertEquals("no permission", (result as IntelligenceApplicationCommandResult.Rejected).reason)
    }

    @Test
    fun `result is deterministic`() {
        val r1 = boundary.success("op1")
        val r2 = boundary.success("op1")

        assertEquals(r1.operationId, r2.operationId)
    }
}
