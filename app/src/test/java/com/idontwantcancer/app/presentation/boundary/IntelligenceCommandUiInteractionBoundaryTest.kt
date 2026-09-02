package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandUiInteractionBoundaryTest {

    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val idempotencyBoundary = mockk<IntelligenceCommandIdempotencyBoundary>(relaxed = true)
    private val dispatchAuthorizationHandover = mockk<IntelligenceCommandDispatchAuthorizationHandoverBoundary>(relaxed = true)
    private val authExecutionHandover = mockk<IntelligenceCommandAuthorizationExecutionHandoverBoundary>(relaxed = true)
    private val terminalIntegrityBoundary = mockk<IntelligenceCommandTerminalStateIntegrityBoundary>(relaxed = true)
    private val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)
    private val dispatcher = DefaultIntelligenceCommandDispatcher(lifecycleBoundary, idempotencyBoundary, dispatchAuthorizationHandover, authExecutionHandover, terminalIntegrityBoundary)

    @Test
    fun `user interaction routes to dispatcher and does not implicitly finalize or close`() = runTest {
        val interaction = IntelligenceUiInteraction.RetryOperation
        
        // Mock idempotency to permit the interaction
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
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { }

        dispatcher.dispatch(request)

        // Verify standard lifecycle transitions
        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.RECEIVED) }
        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.ACCEPTED) }
        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED) }
        
        // Verify Execution Boundary was called
        verify { authExecutionHandover.routeToExecution(request, any()) }

        // EXCLUSION VERIFICATION (Step 138 Invariant):
        // No implicit transition to terminal stages like COMPLETED or CANCELLED 
        // from the dispatcher itself. Those must come from the establishment/finalization authorities.
        verify(exactly = 0) { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.COMPLETED) }
        verify(exactly = 0) { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.CANCELLED) }
    }

    @Test
    fun `viewing signal details is a navigation interaction and does not mutate lifecycle status`() = runTest {
        every { dispatchAuthorizationHandover.routeToAuthorization(any()) } returns IntelligenceCommandAuthorizationResult(
            commandIdentity = "test",
            status = IntelligenceCommandAuthorizationStatus.AUTHORIZED,
            evaluatedAt = Instant.now()
        )
        every { terminalIntegrityBoundary.verifyIntegrity(any()) } returns true
        
        val interaction = IntelligenceUiInteraction.ViewSignalDetails("sig1")
        val onNavigate = mockk<(Any) -> Unit>(relaxed = true)
        val request = IntelligenceCommandDispatchRequest(interaction, handler, onNavigate)

        dispatcher.dispatch(request)

        // Verify navigation occurred
        verify { onNavigate(any()) }
        
        // Verify only operational lifecycle stages (received/accepted/dispatched)
        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED) }
        
        // No closure or finality
        verify(exactly = 0) { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.COMPLETED) }
    }
}
