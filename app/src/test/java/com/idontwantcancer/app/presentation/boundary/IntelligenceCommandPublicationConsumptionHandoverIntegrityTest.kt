package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandPublicationConsumptionHandoverIntegrityTest {

    private val boundary = DefaultIntelligenceCommandPublicationConsumptionHandoverBoundary()

    @Test
    fun `handover preserves publication identity and outcome`() {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op101")
        val publicationResult = IntelligenceCommandPublicationResult(
            operationId = "op101",
            status = CommandPublicationStatus.PUBLISHED
        )
        val request = IntelligenceCommandPublicationConsumptionHandoverRequest(result, publicationResult)
        
        val consumptionRequest = boundary.routeToConsumption(request)

        assertEquals("op101", consumptionRequest.result.operationId)
        assertEquals(CommandPublicationStatus.PUBLISHED, consumptionRequest.publicationResult.status)
    }
}
