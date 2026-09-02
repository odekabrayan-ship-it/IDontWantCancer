package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandConsumptionAcknowledgementHandoverBoundaryTest {

    private val acknowledgementAuthority = mockk<IntelligenceCommandConsumptionAcknowledgementBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandConsumptionAcknowledgementHandoverBoundary(acknowledgementAuthority)

    @Test
    fun `routeToAcknowledgement maps consumption outcome to handover request and delegates to bridge`() = runTest {
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
        
        coEvery { acknowledgementAuthority.routeToAcknowledgement(any()) } returns acknowledgementResult

        val actual = boundary.routeToAcknowledgement(request)

        assertEquals(acknowledgementResult, actual)
        coVerify { acknowledgementAuthority.routeToAcknowledgement(match { it.result == result && it.consumptionRequest == consumptionRequest }) }
    }
}
