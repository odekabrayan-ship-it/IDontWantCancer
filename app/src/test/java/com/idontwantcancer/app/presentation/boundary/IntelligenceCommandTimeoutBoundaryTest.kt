package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import org.junit.Assert.assertNull
import org.junit.Test

class IntelligenceCommandTimeoutBoundaryTest {

    private val boundary = DefaultIntelligenceCommandTimeoutBoundary()

    @Test
    fun `no policy currently exists for standard interactions`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val policy = boundary.getTimeoutPolicy(interaction)

        assertNull(policy)
    }

    @Test
    fun `repeated check is deterministic`() {
        val interaction = IntelligenceUiInteraction.ClearSearch
        val p1 = boundary.getTimeoutPolicy(interaction)
        val p2 = boundary.getTimeoutPolicy(interaction)

        assertNull(p1)
        assertNull(p2)
    }
}
