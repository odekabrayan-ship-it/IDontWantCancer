package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IntelligenceCommandInteractionLoopCompletionBoundaryTest {

    private val boundary = DefaultIntelligenceCommandInteractionLoopCompletionBoundary()

    @Test
    fun `completeLoop establishes synchronized completion result`() {
        val finalityResult = IntelligenceCommandConsumptionFinalityResult(
            operationId = "op1",
            finality = CommandConsumptionFinality.TERMINAL
        )

        val completion = boundary.completeLoop(finalityResult)

        assertEquals("op1", completion.operationId)
        assertEquals(CommandConsumptionFinality.TERMINAL, completion.finality)
        assertTrue(completion.isSynchronized)
    }
}
