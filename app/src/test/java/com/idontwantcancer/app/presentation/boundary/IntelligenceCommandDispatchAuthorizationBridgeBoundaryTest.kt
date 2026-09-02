package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandDispatchAuthorizationBridgeBoundaryTest {

    private val authorizationAuthority = mockk<IntelligenceCommandDispatchAuthorizationBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandDispatchAuthorizationBridgeBoundary(authorizationAuthority)

    @Test
    fun `routeToAuthorization maps handover request and delegates to authority`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val dispatchRequest = IntelligenceCommandDispatchRequest(interaction, handler) { }
        val request = IntelligenceCommandDispatchAuthorizationHandoverRequest(dispatchRequest)
        
        val expectedResult = IntelligenceCommandAuthorizationResult(
            commandIdentity = "id1",
            status = IntelligenceCommandAuthorizationStatus.AUTHORIZED,
            evaluatedAt = Instant.now()
        )
        
        every { authorizationAuthority.routeToAuthorization(dispatchRequest) } returns expectedResult

        val actual = boundary.routeToAuthorization(request)

        assertEquals(expectedResult, actual)
        verify { authorizationAuthority.routeToAuthorization(dispatchRequest) }
    }
}
