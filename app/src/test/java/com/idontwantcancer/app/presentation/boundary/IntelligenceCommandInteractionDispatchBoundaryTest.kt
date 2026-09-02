package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandInteractionDispatchBoundaryTest {

    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val idempotencyBoundary = mockk<IntelligenceCommandIdempotencyBoundary>(relaxed = true)
    private val dispatchAuthorizationHandover = mockk<IntelligenceCommandDispatchAuthorizationHandoverBoundary>(relaxed = true)
    private val authExecutionHandover = mockk<IntelligenceCommandAuthorizationExecutionHandoverBoundary>(relaxed = true)
    private val terminalIntegrityBoundary = mockk<IntelligenceCommandTerminalStateIntegrityBoundary>(relaxed = true)
    private val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
    
    private val dispatcher = DefaultIntelligenceCommandDispatcher(
        lifecycleBoundary, 
        idempotencyBoundary, 
        dispatchAuthorizationHandover, 
        authExecutionHandover, 
        terminalIntegrityBoundary
    )
    
    private val boundary: IntelligenceCommandInteractionDispatchBoundary = dispatcher

    @Test
    fun `dispatchInteraction maps interaction to formalized request and routes to dispatcher`() = runTest {
        val interaction = IntelligenceUiInteraction.RetryOperation
        
        every { idempotencyBoundary.evaluateIdempotency(any(), any()) } returns IntelligenceCommandIdempotencyResult(
            commandIdentity = "test",
            status = CommandIdempotencyStatus.PROCEED,
            evaluatedAt = Instant.now()
        )
        
        every { dispatchAuthorizationHandover.routeToAuthorization(any()) } returns IntelligenceCommandAuthorizationResult(
            commandIdentity = "test",
            status = IntelligenceCommandAuthorizationStatus.AUTHORIZED,
            evaluatedAt = Instant.now()
        )

        every { terminalIntegrityBoundary.verifyIntegrity(any()) } returns true

        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandDispatchRequest(interaction, handler, onNavigate)
        
        // This is the Step 154 logic test: Ensuring the bridge maps and calls
        boundary.dispatchInteraction(request)

        // Verify the existing dispatcher's logic was executed (Standard dispatch path)
        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED) }
        verify { authExecutionHandover.routeToExecution(any(), any()) }
    }
}
