package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandConsumptionAcknowledgementBoundaryTest {

    private val acknowledgementAuthority = mockk<IntelligenceCommandResultAcknowledgementBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandConsumptionAcknowledgementBoundary(acknowledgementAuthority)

    @Test
    fun `routeToAcknowledgement maps handover request to internal acknowledgement request and delegates`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val consumptionRequest = IntelligenceCommandConsumptionRequest(
            result = result,
            publicationResult = IntelligenceCommandPublicationResult(
                operationId = "op1",
                status = CommandPublicationStatus.PUBLISHED
            )
        )
        val request = IntelligenceCommandConsumptionAcknowledgementHandoverRequest(result, consumptionRequest)
        
        val acknowledgementResult = IntelligenceCommandAcknowledgementResult(
            operationId = "op1",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED
        )
        
        coEvery { acknowledgementAuthority.acknowledge(any()) } returns acknowledgementResult

        val actual = boundary.routeToAcknowledgement(request)

        assertEquals(acknowledgementResult, actual)
        coVerify { acknowledgementAuthority.acknowledge(match { it.result == result }) }
    }
}
