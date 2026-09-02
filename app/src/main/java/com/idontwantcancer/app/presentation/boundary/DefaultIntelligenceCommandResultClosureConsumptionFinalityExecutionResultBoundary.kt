package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionOutcome
import javax.inject.Inject

/**
 * Default implementation of the execution result boundary.
 * Bridges raw outcomes to the authoritative result established in Step 112.
 */
class DefaultIntelligenceCommandResultClosureConsumptionFinalityExecutionResultBoundary @Inject constructor(
    private val resultAuthority: IntelligenceCommandResultBoundary,
    private val failureBoundary: IntelligenceCommandFailureBoundary
) : IntelligenceCommandResultClosureConsumptionFinalityExecutionResultBoundary {

    override fun establishResult(
        outcome: IntelligenceCommandExecutionOutcome
    ): IntelligenceApplicationCommandResult {
        // Step 142 Logic: Convert execution outcome into the authoritative result.
        // Delegates strictly to Step 112 to preserve result truth authority.
        return when (outcome) {
            is IntelligenceCommandExecutionOutcome.Success -> 
                resultAuthority.success(outcome.operationId)
            
            is IntelligenceCommandExecutionOutcome.Failure -> 
                failureBoundary.mapFailure(outcome.throwable, outcome.operationId)
            
            is IntelligenceCommandExecutionOutcome.Rejected -> 
                resultAuthority.rejected(outcome.reason, outcome.operationId)
            
            is IntelligenceCommandExecutionOutcome.Cancelled -> 
                resultAuthority.cancelled(outcome.operationId)
            
            is IntelligenceCommandExecutionOutcome.TimedOut -> 
                resultAuthority.timedOut(outcome.operationId)
        }
    }
}
