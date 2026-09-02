package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandExecutionResultHandoverBoundaryTest {

    private val resultBridge = mockk<IntelligenceCommandExecutionResultBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandExecutionResultHandoverBoundary(resultBridge)

    @Test
    fun `routeToResult maps interaction and outcome to handover request and delegates to bridge`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val outcome = IntelligenceCommandExecutionOutcome.Success("op1")
        
        val expectedResult = IntelligenceApplicationCommandResult.Success(
            operationId = "op1"
        )
        
        every { resultBridge.routeToResult(any()) } returns expectedResult

        val actual = boundary.routeToResult(interaction, outcome)

        assertEquals(expectedResult, actual)
        verify { resultBridge.routeToResult(match { it.interaction == interaction && it.outcome == outcome }) }
    }
}
