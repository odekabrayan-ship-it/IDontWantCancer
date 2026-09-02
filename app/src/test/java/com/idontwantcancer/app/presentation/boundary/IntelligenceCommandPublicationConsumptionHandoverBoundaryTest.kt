package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandPublicationConsumptionHandoverBoundaryTest {

    private val boundary = DefaultIntelligenceCommandPublicationConsumptionHandoverBoundary()

    @Test
    fun `routeToConsumption maps publication result to handover request and delegates to consumption`() {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val publicationResult = IntelligenceCommandPublicationResult(
            operationId = "op1",
            status = CommandPublicationStatus.PUBLISHED
        )
        val request = IntelligenceCommandPublicationConsumptionHandoverRequest(result, publicationResult)

        val consumptionRequest = boundary.routeToConsumption(request)

        assertEquals(result, consumptionRequest.result)
        assertEquals(publicationResult, consumptionRequest.publicationResult)
    }
}
