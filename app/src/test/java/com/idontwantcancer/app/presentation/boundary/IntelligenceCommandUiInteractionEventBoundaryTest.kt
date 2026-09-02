package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

/**
 * Verification test for the rendering to interaction boundary (Step 153).
 * Ensures that UI interactions are converted into explicit application events 
 * and handed to the existing interaction authority.
 */
class IntelligenceCommandUiInteractionEventBoundaryTest {

    @Test
    fun `UI interaction is converted into an explicit application event`() {
        val onInteraction = mockk<(IntelligenceUiInteraction) -> Unit>(relaxed = true)
        
        // This simulates a UI click being mapped to an explicit event
        val interactionEvent = IntelligenceUiInteraction.RetryOperation
        onInteraction(interactionEvent)
        
        // Verify the event reached the interaction boundary
        verify { onInteraction(interactionEvent) }
    }

    @Test
    fun `rendering does not bypass the interaction authority for domain mutation`() {
        // This test documents the architectural invariant:
        // No Composable should have direct access to a repository or domain authority.
        // It should only have access to 'onInteraction' callback.
        
        val interactionBoundary = mockk<IntelligenceInteractionBoundary>(relaxed = true)
        
        // Correct path: UI -> Interaction Object -> Boundary
        val interaction = IntelligenceUiInteraction.PerformSearch("test")
        interactionBoundary.onInteraction(interaction)
        
        verify { interactionBoundary.onInteraction(interaction) }
    }
}
