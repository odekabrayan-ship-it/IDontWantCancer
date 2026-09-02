package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Default implementation of the command finalization boundary.
 * Coordinates lifecycle terminal stages and ensures exactly-once finalization 
 * per operation identity.
 */
@Singleton
class DefaultIntelligenceCommandFinalizationBoundary @Inject constructor(
    private val lifecycleBoundary: IntelligenceCommandLifecycleBoundary,
    private val handoffBoundary: IntelligenceCommandResultHandoffBoundary,
    private val terminalIntegrityBoundary: IntelligenceCommandTerminalStateIntegrityBoundary
) : IntelligenceCommandFinalizationBoundary {

    // Step 123: Process-safe scope for handoff routing
    private val scope = MainScope()

    override fun finalizeResult(
        interaction: IntelligenceUiInteraction,
        result: IntelligenceApplicationCommandResult
    ): IntelligenceApplicationCommandResult {
        // 1. Identify Command (Step 115 principle)
        val identity = result.operationId ?: interaction.hashCode().toString()

        // 2. Protect Terminal-State Integrity (Step 149 Logic)
        val verifiedResult = terminalIntegrityBoundary.protectTerminality(identity, result)
        
        // Exactly-Once execution path: if verifiedResult != result, we are handling a duplicate.
        if (verifiedResult !== result) {
            return verifiedResult
        }

        // 3. Map Result to Terminal Lifecycle Stage (Step 113)
        val stage = when (result) {
            is IntelligenceApplicationCommandResult.Success,
            is IntelligenceApplicationCommandResult.Failure,
            is IntelligenceApplicationCommandResult.Rejected -> CommandLifecycleStage.COMPLETED
            
            is IntelligenceApplicationCommandResult.Cancelled -> CommandLifecycleStage.CANCELLED
            is IntelligenceApplicationCommandResult.TimedOut -> CommandLifecycleStage.TIMED_OUT
        }

        lifecycleBoundary.transitionTo(interaction, stage)

        // 4. Hand off to downstream authority (Step 123)
        scope.launch {
            handoffBoundary.performHandoff(result)
        }

        return result
    }
}
