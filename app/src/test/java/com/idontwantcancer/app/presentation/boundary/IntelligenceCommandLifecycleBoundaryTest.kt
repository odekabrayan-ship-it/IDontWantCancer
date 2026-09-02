package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandLifecycleStage
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandLifecycleBoundaryTest {

    private val boundary = DefaultIntelligenceCommandLifecycleBoundary()

    @Test
    fun `transition produces deterministic status record`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val stage = CommandLifecycleStage.PROCESSING

        val status = boundary.transitionTo(interaction, stage)

        assertEquals(interaction, status.interaction)
        assertEquals(stage, status.stage)
    }

    @Test
    fun `repeated transitions are independent and deterministic`() {
        val interaction = IntelligenceUiInteraction.ClearSearch
        
        val s1 = boundary.transitionTo(interaction, CommandLifecycleStage.RECEIVED)
        val s2 = boundary.transitionTo(interaction, CommandLifecycleStage.ACCEPTED)

        assertEquals(CommandLifecycleStage.RECEIVED, s1.stage)
        assertEquals(CommandLifecycleStage.ACCEPTED, s2.stage)
        assertEquals(interaction, s1.interaction)
        assertEquals(interaction, s2.interaction)
    }
}
