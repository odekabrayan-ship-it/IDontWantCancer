package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest
import io.mockk.*
import org.junit.Test

class IntelligenceCommandRenderingLifecycleBridgeBoundaryTest {

    private val lifecycleAuthority = mockk<IntelligenceCommandRenderingLifecycleBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandRenderingLifecycleBridgeBoundary(lifecycleAuthority)

    @Test
    fun `routeToLifecycle passes formalized request to authoritative gate`() {
        val request = mockk<IntelligenceCommandRenderingLifecycleHandoverRequest>()
        
        boundary.routeToLifecycle(request)

        verify { lifecycleAuthority.participate(request) }
    }
}
