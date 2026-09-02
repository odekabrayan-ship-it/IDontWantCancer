package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandExecutionResultBridgeBoundaryTest {

    private val resultAuthority = mockk<IntelligenceCommandExecutionOutcomeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandExecutionResultBridgeBoundary(resultAuthority)

    @Test
    fun `routeToResult maps handover request to internal result request and delegates`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val outcome = IntelligenceCommandExecutionOutcome.Success("op1")
        val request = IntelligenceCommandExecutionResultHandoverRequest(interaction, outcome)
        
        val expectedResult = IntelligenceApplicationCommandResult.Success(
            operationId = "op1",
            processedAt = Instant.now()
        )
        
        every { resultAuthority.reportOutcome(interaction, outcome) } returns expectedResult

        val actual = boundary.routeToResult(request)

        assertEquals(expectedResult, actual)
        verify { resultAuthority.reportOutcome(interaction, outcome) }
    }
}
