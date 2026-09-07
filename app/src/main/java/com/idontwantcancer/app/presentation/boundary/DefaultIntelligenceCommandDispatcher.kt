package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import com.idontwantcancer.app.presentation.navigation.Screen
import javax.inject.Inject

/**
 * Default implementation of the command dispatcher.
 * Handles the mapping between UI interactions and navigation/handler actions.
 */
class DefaultIntelligenceCommandDispatcher @Inject constructor(
    private val lifecycleBoundary: IntelligenceCommandLifecycleBoundary,
    private val idempotencyBoundary: IntelligenceCommandIdempotencyBoundary,
    private val dispatchAuthorizationHandover: IntelligenceCommandDispatchAuthorizationHandoverBoundary,
    private val authExecutionHandover: IntelligenceCommandAuthorizationExecutionHandoverBoundary,
    private val terminalIntegrityBoundary: IntelligenceCommandTerminalStateIntegrityBoundary
) : IntelligenceCommandDispatcher, 
    IntelligenceCommandInteractionDispatchBoundary {

    override fun dispatchInteraction(
        request: IntelligenceCommandDispatchRequest
    ) {
        // Step 154 Logic: Route the request to the main dispatch logic.
        dispatch(request)
    }

    override fun dispatch(request: IntelligenceCommandDispatchRequest) {
        val interaction = request.interaction
        val handler = request.handler
        val onNavigate = request.onNavigate

        // Step 115: Command Idempotency Evaluation
        val commandIdentity = interaction.hashCode().toString()
        val idempotency = idempotencyBoundary.evaluateIdempotency(interaction, commandIdentity)
        
        if (idempotency.status == CommandIdempotencyStatus.DUPLICATE_DETECTED) {
            // Ignore if authoritative semantics require it
            return
        }

        // Step 149: Terminal-State Integrity Check
        // Prevent re-dispatching if the command has already reached its absolute terminal point.
        if (!terminalIntegrityBoundary.verifyIntegrity(commandIdentity)) {
            return
        }

        // Step 155 / 170 / 185: Dispatch → Authorization Bridge
        val authorization = dispatchAuthorizationHandover.routeToAuthorization(request)
        
        if (authorization.status != IntelligenceCommandAuthorizationStatus.AUTHORIZED) {
            // Record failure in lifecycle before rejecting
            lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.RECEIVED)
            // Implicit rejection by not proceeding to DISPATCHED
            return
        }

        // Step 113: RECEIVED -> ACCEPTED
        lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.RECEIVED)
        lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.ACCEPTED)

        when (interaction) {
            is IntelligenceUiInteraction.ViewSignalDetails -> {
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                onNavigate(Screen.SignalDetail(interaction.signalId))
            }
            is IntelligenceUiInteraction.EnterAlerts -> {
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                onNavigate(Screen.Alerts)
            }
            is IntelligenceUiInteraction.EnterVerify -> {
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                onNavigate(Screen.Search(interaction.storeMode))
            }
            is IntelligenceUiInteraction.EnterPrevention -> {
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                onNavigate(Screen.Prevention)
            }
            is IntelligenceUiInteraction.EnterHealingSanctuary -> {
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                onNavigate(Screen.HealingSanctuary)
            }
            is IntelligenceUiInteraction.EnterMyJourney -> {
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                onNavigate(Screen.MyJourney)
            }
            is IntelligenceUiInteraction.EnterSettings -> {
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                onNavigate(Screen.Settings)
            }
            is IntelligenceUiInteraction.NavigateBack -> {
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                onNavigate(NavigateBackAction)
            }
            is IntelligenceUiInteraction.CancelOperation -> {
                // Step 114: Cancellation Handoff
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                authExecutionHandover.routeToExecution(request, authorization)
            }
            else -> {
                // Forward all other interactions to the authoritative handler (ViewModel)
                lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.DISPATCHED)
                authExecutionHandover.routeToExecution(request, authorization)
            }
        }
    }

    /**
     * Internal signal for back navigation.
     */
    object NavigateBackAction
}
