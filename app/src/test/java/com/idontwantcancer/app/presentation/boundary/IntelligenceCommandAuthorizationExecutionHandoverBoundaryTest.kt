package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test

class IntelligenceCommandAuthorizationExecutionHandoverBoundaryTest {

    private val executionBridge = mockk<IntelligenceCommandAuthorizationExecutionBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandAuthorizationExecutionHandoverBoundary(executionBridge)

    @Test
    fun `routeToExecution maps authorization to handover request and delegates to bridge`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { }
        val authorization = IntelligenceCommandAuthorizationResult(
            commandIdentity = "test_id",
            status = IntelligenceCommandAuthorizationStatus.AUTHORIZED
        )
        
        boundary.routeToExecution(request, authorization)

        verify { 
            executionBridge.routeToExecution(match { 
                it.dispatchRequest == request && it.authorizationResult == authorization 
            }) 
        }
    }
}
