package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandConsumptionAcknowledgementHandoverIntegrityTest {

    private val acknowledgementBridge = mockk<IntelligenceCommandConsumptionAcknowledgementBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandConsumptionAcknowledgementHandoverBoundary(acknowledgementBridge)

    @Test
    fun `handover preserves consumption identity and outcome`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op111")
        val consumptionRequest = IntelligenceCommandConsumptionRequest(
            result = result,
            publicationResult = IntelligenceCommandPublicationResult(
                operationId = "op111",
                status = CommandPublicationStatus.PUBLISHED
            )
        )
        val request = IntelligenceCommandConsumptionAcknowledgementHandoverRequest(result, consumptionRequest)
        
        val expectedResult = IntelligenceCommandAcknowledgementResult(
            operationId = "op111",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED
        )
        
        coEvery { acknowledgementBridge.routeToAcknowledgement(any()) } returns expectedResult

        val actual = boundary.routeToAcknowledgement(request)

        assertEquals(expectedResult, actual)
        coVerify { 
            acknowledgementBridge.routeToAcknowledgement(match { 
                it.result == result && it.consumptionRequest == consumptionRequest 
            }) 
        }
    }
}
