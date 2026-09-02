package com.idontwantcancer.app.core.ui.theme

import com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayoutType
import org.junit.Assert.assertEquals
import org.junit.Test

class AdaptiveInformationDensityIntegrityTest {

    @Test
    fun `spacing increases with layout expansion`() {
        val compact = getSpacingFor(AdaptiveLayoutType.Compact)
        val medium = getSpacingFor(AdaptiveLayoutType.Medium)
        val expanded = getSpacingFor(AdaptiveLayoutType.Expanded)
        
        // screenPadding should increase
        assert(expanded.screenPadding > compact.screenPadding)
        assert(medium.screenPadding >= compact.screenPadding)
        
        // sectionSpacing should increase
        assert(expanded.sectionSpacing > compact.sectionSpacing)
        
        // cardPadding should increase
        assert(expanded.cardPadding > compact.cardPadding)
    }

    @Test
    fun `typography remains stable across layout changes`() {
        // Typography is authoritative and independent of window size in this step
        // although it could be adaptive in future, Step 205 focuses on density via spacing
        assertEquals(28, Typography.headlineMedium.fontSize.value.toInt())
    }
}
