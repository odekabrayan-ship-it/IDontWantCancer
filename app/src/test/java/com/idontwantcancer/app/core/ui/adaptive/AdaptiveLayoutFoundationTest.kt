package com.idontwantcancer.app.core.ui.adaptive

import org.junit.Assert.assertEquals
import org.junit.Test

class AdaptiveLayoutFoundationTest {

    @Test
    fun `AdaptiveLayoutType has three canonical entries`() {
        assertEquals(3, AdaptiveLayoutType.entries.size)
        assertEquals(AdaptiveLayoutType.Compact, AdaptiveLayoutType.entries[0])
        assertEquals(AdaptiveLayoutType.Medium, AdaptiveLayoutType.entries[1])
        assertEquals(AdaptiveLayoutType.Expanded, AdaptiveLayoutType.entries[2])
    }

    @Test
    fun `AdaptiveLayoutType enum mapping integrity`() {
        val type = AdaptiveLayoutType.Compact
        assertEquals("Compact", type.name)
    }
}
