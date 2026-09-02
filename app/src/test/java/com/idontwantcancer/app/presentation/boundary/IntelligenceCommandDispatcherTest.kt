package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import com.idontwantcancer.app.presentation.navigation.Screen
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant

class IntelligenceCommandDispatcherTest {

    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val idempotencyBoundary = mockk<IntelligenceCommandIdempotencyBoundary>(relaxed = true)
    private val dispatchAuthorizationHandover = mockk<IntelligenceCommandDispatchAuthorizationHandoverBoundary>(relaxed = true)
    private val authExecutionHandover = mockk<IntelligenceCommandAuthorizationExecutionHandoverBoundary>(relaxed = true)
    private val terminalIntegrityBoundary = mockk<IntelligenceCommandTerminalStateIntegrityBoundary>(relaxed = true)
    private val dispatcher = DefaultIntelligenceCommandDispatcher(lifecycleBoundary, idempotencyBoundary, dispatchAuthorizationHandover, authExecutionHandover, terminalIntegrityBoundary)
    private val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)

    @Before
    fun setup() {
        every { idempotencyBoundary.evaluateIdempotency(any(), any()) } returns IntelligenceCommandIdempotencyResult(
            commandIdentity = "id",
            status = CommandIdempotencyStatus.PROCEED,
            evaluatedAt = Instant.now()
        )
        every { terminalIntegrityBoundary.verifyIntegrity(any()) } returns true
        every { dispatchAuthorizationHandover.routeToAuthorization(any()) } returns IntelligenceCommandAuthorizationResult(
            commandIdentity = "id",
            status = IntelligenceCommandAuthorizationStatus.AUTHORIZED,
            evaluatedAt = Instant.now()
        )
    }

    @Test
    fun `ViewSignalDetails interaction routes to navigation`() {
        val interaction = IntelligenceUiInteraction.ViewSignalDetails("sig1")
        var navigatedTo: Any? = null
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { navigatedTo = it }

        dispatcher.dispatch(request)

        assertTrue(navigatedTo is Screen.SignalDetail)
        assertEquals("sig1", (navigatedTo as Screen.SignalDetail).signalId)
    }

    @Test
    fun `NavigateBack interaction routes to back action`() {
        val interaction = IntelligenceUiInteraction.NavigateBack
        var navigatedTo: Any? = null
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { navigatedTo = it }

        dispatcher.dispatch(request)

        assertEquals(DefaultIntelligenceCommandDispatcher.NavigateBackAction, navigatedTo)
    }

    @Test
    fun `RetryOperation interaction routes to execution boundary`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { }

        dispatcher.dispatch(request)

        verify { authExecutionHandover.routeToExecution(request, any()) }
    }

    @Test
    fun `PerformSearch interaction routes to execution boundary`() {
        val interaction = IntelligenceUiInteraction.PerformSearch("test")
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { }

        dispatcher.dispatch(request)

        verify { authExecutionHandover.routeToExecution(request, any()) }
    }

    @Test
    fun `duplicate command detected by boundary is ignored`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        every { idempotencyBoundary.evaluateIdempotency(any(), any()) } returns IntelligenceCommandIdempotencyResult(
            commandIdentity = "id",
            status = CommandIdempotencyStatus.DUPLICATE_DETECTED,
            evaluatedAt = Instant.now()
        )
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { }

        dispatcher.dispatch(request)

        // Should not reach the lifecycle boundary or handler
        verify(exactly = 0) { lifecycleBoundary.transitionTo(any(), any()) }
        verify(exactly = 0) { handler.onInteraction(any()) }
    }

    @Test
    fun `terminal command detected by boundary is ignored`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        
        every { terminalIntegrityBoundary.verifyIntegrity(any()) } returns false
        val request = IntelligenceCommandDispatchRequest(interaction, handler) { }

        dispatcher.dispatch(request)

        // Should not reach authorization or handler
        verify(exactly = 0) { dispatchAuthorizationHandover.routeToAuthorization(any()) }
        verify(exactly = 0) { handler.onInteraction(any()) }
    }
}
