package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.alerts.AlertsUiState
import com.idontwantcancer.app.presentation.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

class AdaptiveStateContinuityIntegrityTest {

    @Test
    fun `selection state remains stable during simulated layout change`() {
        val signalId = "authoritative_sig_204"
        
        // Initial state with selection
        val initialState = AlertsUiState.Success(
            signals = emptyList(),
            selectedSignalId = signalId
        )
        
        // Simulate a "re-generation" of UI state which happens on layout changes
        // (e.g. when ViewModel updates its state based on external factors but preserves selection)
        val newState = initialState.copy(
            finality = CommandConsumptionFinalityPresentationContract.StatusUnavailable
        )
        
        // Verify selection identity is preserved
        assertEquals(signalId, newState.selectedSignalId)
    }

    @Test
    fun `interaction identity is independent of animation state`() {
        val interaction = IntelligenceUiInteraction.ViewSignalDetails("id1")
        
        // Even if we are in the middle of a transition, the identity is what matters
        val request = IntelligenceCommandScreenLifecycleInteractionHandoverRequest(
            interaction = interaction,
            handler = io.mockk.mockk(relaxed = true),
            onNavigate = {}
        )
        
        assertEquals("id1", (request.interaction as IntelligenceUiInteraction.ViewSignalDetails).signalId)
    }
}
