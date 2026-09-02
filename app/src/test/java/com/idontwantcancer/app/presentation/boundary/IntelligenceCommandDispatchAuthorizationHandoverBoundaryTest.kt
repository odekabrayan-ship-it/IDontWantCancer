package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandDispatchAuthorizationHandoverBoundaryTest {

    private val authorizationBridge = mockk<IntelligenceCommandDispatchAuthorizationBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandDispatchAuthorizationHandoverBoundary(authorizationBridge)

    @Test
    fun `routeToAuthorization maps dispatch request to handover request and delegates to bridge`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandDispatchRequest(interaction, handler, onNavigate)
        
        val expectedResult = IntelligenceCommandAuthorizationResult(
            status = IntelligenceCommandAuthorizationStatus.AUTHORIZED,
            commandIdentity = "test_cmd"
        )
        
        every { authorizationBridge.routeToAuthorization(any()) } returns expectedResult

        val actual = boundary.routeToAuthorization(request)

        assertEquals(expectedResult, actual)
        verify { authorizationBridge.routeToAuthorization(match { it.dispatchRequest == request }) }
    }
}
