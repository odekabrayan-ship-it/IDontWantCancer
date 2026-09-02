package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandResultClosureConsumptionFinalityDispatchAuthorizationBoundaryTest {

    private val boundary = DefaultIntelligenceCommandResultClosureConsumptionFinalityDispatchAuthorizationBoundary()

    @Test
    fun `standard interactions are AUTHORIZED by default`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val request = IntelligenceCommandAuthorizationRequest(interaction, "test_id")
        val result = boundary.evaluateAuthorization(request)

        assertEquals(IntelligenceCommandAuthorizationStatus.AUTHORIZED, result.status)
        assertEquals("test_id", result.commandIdentity)
    }

    @Test
    fun `evaluation is deterministic across identical interactions`() {
        val interaction = IntelligenceUiInteraction.ClearSearch
        val request = IntelligenceCommandAuthorizationRequest(interaction, "id1")
        val r1 = boundary.evaluateAuthorization(request)
        val r2 = boundary.evaluateAuthorization(request)

        assertEquals(r1.status, r2.status)
        assertEquals(r1.commandIdentity, r2.commandIdentity)
    }
}
