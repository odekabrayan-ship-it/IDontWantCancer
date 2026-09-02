package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandRetryStatus
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandRetryBoundaryTest {

    private val boundary = DefaultIntelligenceCommandRetryBoundary()

    @Test
    fun `standard UI interaction results in EXTERNALLY_MANAGED retry status`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val exception = RuntimeException("Fail")
        
        val result = boundary.evaluateRetry(interaction, exception, 0)

        assertEquals(CommandRetryStatus.EXTERNALLY_MANAGED, result.status)
    }

    @Test
    fun `retry evaluation is deterministic`() {
        val interaction = IntelligenceUiInteraction.PerformSearch("test")
        val exception = RuntimeException("Fail")
        
        val r1 = boundary.evaluateRetry(interaction, exception, 1)
        val r2 = boundary.evaluateRetry(interaction, exception, 1)

        assertEquals(r1.status, r2.status)
        assertEquals(r1.attemptCount, r2.attemptCount)
    }
}
