package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandFinalizationBoundaryTest {

    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val handoffBoundary = mockk<IntelligenceCommandResultHandoffBoundary>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val terminalIntegrityBoundary = DefaultIntelligenceCommandTerminalStateIntegrityBoundary()
    private val finalizer = DefaultIntelligenceCommandFinalizationBoundary(lifecycleBoundary, handoffBoundary, terminalIntegrityBoundary)

    @Test
    fun `successful result finalizes to COMPLETED lifecycle stage`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")

        val finalized = finalizer.finalizeResult(interaction, result)

        assertEquals(result, finalized)
        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.COMPLETED) }
    }

    @Test
    fun `cancelled result finalizes to CANCELLED lifecycle stage`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val result = IntelligenceApplicationCommandResult.Cancelled(operationId = "op2")

        finalizer.finalizeResult(interaction, result)

        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.CANCELLED) }
    }

    @Test
    fun `timeout result finalizes to TIMED_OUT lifecycle stage`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val result = IntelligenceApplicationCommandResult.TimedOut(operationId = "op3")

        finalizer.finalizeResult(interaction, result)

        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.TIMED_OUT) }
    }

    @Test
    fun `repeated finalization for same operation ID returns first result and does not re-transition`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val r1 = IntelligenceApplicationCommandResult.Success(operationId = "op4")
        val r2 = IntelligenceApplicationCommandResult.TimedOut(operationId = "op4") // Late timeout

        val finalized1 = finalizer.finalizeResult(interaction, r1)
        val finalized2 = finalizer.finalizeResult(interaction, r2)

        assertEquals(r1, finalized1)
        assertEquals(r1, finalized2) // r1 was first
        
        // Should only have transitioned once
        verify(exactly = 1) { lifecycleBoundary.transitionTo(interaction, any()) }
    }
}
