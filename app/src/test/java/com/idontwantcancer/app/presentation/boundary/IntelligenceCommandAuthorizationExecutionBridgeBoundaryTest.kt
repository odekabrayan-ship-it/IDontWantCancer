package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import org.junit.Test
import java.time.Instant

class IntelligenceCommandAuthorizationExecutionBridgeBoundaryTest {

    private val executionAuthority = mockk<IntelligenceCommandResultClosureConsumptionFinalityExecutionBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandAuthorizationExecutionBridgeBoundary(executionAuthority)

    @Test
    fun `AUTHORIZED handover request is routed to execution authority`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val dispatchRequest = IntelligenceCommandDispatchRequest(interaction, handler) { }
        
        val authorization = IntelligenceCommandAuthorizationResult(
            commandIdentity = "id1",
            status = IntelligenceCommandAuthorizationStatus.AUTHORIZED,
            evaluatedAt = Instant.now()
        )
        val request = IntelligenceCommandAuthorizationExecutionHandoverRequest(dispatchRequest, authorization)

        boundary.routeToExecution(request)

        verify { executionAuthority.execute(match { it.interaction == interaction && it.handler == handler }) }
    }

    @Test
    fun `DENIED handover request is NOT routed to execution authority`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        val dispatchRequest = IntelligenceCommandDispatchRequest(interaction, handler) { }
        
        val authorization = IntelligenceCommandAuthorizationResult(
            commandIdentity = "id2",
            status = IntelligenceCommandAuthorizationStatus.DENIED,
            evaluatedAt = Instant.now()
        )
        val request = IntelligenceCommandAuthorizationExecutionHandoverRequest(dispatchRequest, authorization)

        boundary.routeToExecution(request)

        verify(exactly = 0) { executionAuthority.execute(any()) }
    }
}
