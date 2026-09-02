package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IntelligenceCommandFailureBoundaryTest {

    private val boundary = DefaultIntelligenceCommandFailureBoundary()

    @Test
    fun `mapFailure maps Exception to structured Failure result`() {
        val exception = RuntimeException("Technical error")
        val result = boundary.mapFailure(exception, "op1")

        assertTrue(result is IntelligenceApplicationCommandResult.Failure)
        assertEquals("Technical error", result.reason)
        assertEquals("op1", result.operationId)
    }

    @Test
    fun `mapFailure provides fallback reason for null message`() {
        val exception = NullPointerException(null)
        val result = boundary.mapFailure(exception, "op1")

        assertTrue(result.reason.isNotEmpty())
        assertEquals("op1", result.operationId)
    }
}
