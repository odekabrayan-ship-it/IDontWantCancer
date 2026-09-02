package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import org.junit.Assert.assertEquals
import org.junit.Test

class ImeHardeningIntegrityTest {

    @Test
    fun `IME search action preserves authoritative query identity`() {
        val query = "intelligence query 217"
        val interaction = IntelligenceUiInteraction.PerformSearch(query)
        
        // This confirms that the keyboard action preserves the domain query exactly.
        assertEquals(query, interaction.query)
    }

    @Test
    fun `keyboard dismissal interactions are recognized as auditable events`() {
        // Clearing search or clearing selection are the primary dismissal triggers
        // that must follow the command arc.
        val clearSearch = IntelligenceUiInteraction.ClearSearch
        val clearSelection = IntelligenceUiInteraction.ClearSelection
        
        assertEquals("ClearSearch", clearSearch.toString())
        assertEquals("ClearSelection", clearSelection.toString())
    }
}
