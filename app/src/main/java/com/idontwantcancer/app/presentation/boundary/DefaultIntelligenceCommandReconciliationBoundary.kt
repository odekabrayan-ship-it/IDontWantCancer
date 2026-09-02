package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command reconciliation boundary.
 * Delegates to authoritative domain boundaries to establish the truth 
 * without guessing.
 */
class DefaultIntelligenceCommandReconciliationBoundary @Inject constructor(
    private val reentryConsumptionBoundary: IntelligenceReentryReconciliationConsumptionBoundary
) : IntelligenceCommandReconciliationBoundary {

    override suspend fun reconcile(
        interaction: IntelligenceUiInteraction,
        commandIdentity: String
    ): IntelligenceCommandReconciliationResult {
        val now = Instant.now()

        // 1. Identify Re-entry Commands (Step 103 Authority)
        if (commandIdentity.contains("::")) {
            val contract = reentryConsumptionBoundary.getReconciliationContract(commandIdentity)
            
            return if (contract != null) {
                IntelligenceCommandReconciliationResult(
                    commandIdentity = commandIdentity,
                    status = if (contract.isConsistent) IntelligenceCommandReconciliationStatus.CONFIRMED_SUCCESS 
                             else IntelligenceCommandReconciliationStatus.CONFIRMED_FAILURE,
                    reason = "Reentry contract verification: ${contract.detail ?: "Consistency check complete."}",
                    evaluatedAt = now
                )
            } else {
                IntelligenceCommandReconciliationResult(
                    commandIdentity = commandIdentity,
                    status = IntelligenceCommandReconciliationStatus.INDETERMINATE,
                    reason = "No authoritative reconciliation contract found for identity $commandIdentity",
                    evaluatedAt = now
                )
            }
        }

        // 2. Default Policy: Ambiguous outcomes for generic UI commands remain indeterminate 
        // unless a specialized authority can confirm the side effect.
        return IntelligenceCommandReconciliationResult(
            commandIdentity = commandIdentity,
            status = IntelligenceCommandReconciliationStatus.INDETERMINATE,
            reason = "Command context does not support automated state confirmation.",
            evaluatedAt = now
        )
    }
}
