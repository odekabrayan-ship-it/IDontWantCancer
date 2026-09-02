package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultClosureConsumptionFinalityExecutionBoundaryTest {

    private val boundary = DefaultIntelligenceCommandResultClosureConsumptionFinalityExecutionBoundary()
    private val handler = mockk<IntelligenceInteractionBoundary>(relaxed = true)

    @Test
    fun `execute performs the operation by delegating to the handler`() {
        val interaction = IntelligenceUiInteraction.RetryOperation
        val request = IntelligenceCommandAuthorizedExecutionRequest(
            interaction = interaction,
            handler = handler,
            authorizationResult = IntelligenceCommandAuthorizationResult(
                commandIdentity = "id1",
                status = IntelligenceCommandAuthorizationStatus.AUTHORIZED,
                evaluatedAt = Instant.now()
            )
        )
        
        boundary.execute(request)

        verify { handler.onInteraction(interaction) }
    }
}
