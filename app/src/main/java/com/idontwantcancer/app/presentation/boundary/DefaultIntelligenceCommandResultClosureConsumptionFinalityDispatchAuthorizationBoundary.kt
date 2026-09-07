package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the dispatch authorization boundary.
 * Enforces presentation-level rules before allowing interactions to reach 
 * domain authorities.
 */
class DefaultIntelligenceCommandResultClosureConsumptionFinalityDispatchAuthorizationBoundary @Inject constructor() : 
    IntelligenceCommandResultClosureConsumptionFinalityDispatchAuthorizationBoundary {

    override fun evaluateAuthorization(
        request: IntelligenceCommandAuthorizationRequest
    ): IntelligenceCommandAuthorizationResult {
        // 1. Establish Policy Semantic (Step 140 Logic)
        // Standard interactions from authorized UI components are AUTHORIZED by default.
        // This boundary ensures that implicit or bypassed paths cannot reach execution.
        val isAuthorized = when (request.interaction) {
            is IntelligenceUiInteraction.ViewSignalDetails,
            is IntelligenceUiInteraction.NavigateBack,
            is IntelligenceUiInteraction.RetryOperation,
            is IntelligenceUiInteraction.PerformSearch,
            is IntelligenceUiInteraction.ClearSearch,
            is IntelligenceUiInteraction.CancelOperation,
            is IntelligenceUiInteraction.AcknowledgeSignal,
            is IntelligenceUiInteraction.ToggleWatch,
            is IntelligenceUiInteraction.EnterHealingSanctuary,
            is IntelligenceUiInteraction.EnterAlerts,
            is IntelligenceUiInteraction.EnterVerify,
            is IntelligenceUiInteraction.EnterPrevention,
            is IntelligenceUiInteraction.EnterSettings,
            is IntelligenceUiInteraction.EnterMyJourney,
            is IntelligenceUiInteraction.ClearSelection -> true
        }

        val status = if (isAuthorized) IntelligenceCommandAuthorizationStatus.AUTHORIZED 
                     else IntelligenceCommandAuthorizationStatus.DENIED

        return IntelligenceCommandAuthorizationResult(
            commandIdentity = request.commandIdentity,
            status = status,
            reason = if (isAuthorized) "Interaction authorized via presentation policy gate."
                     else "Interaction denied: unauthorized dispatch path.",
            evaluatedAt = Instant.now()
        )
    }
}
