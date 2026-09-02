package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Default implementation of the command result consumption boundary.
 * Provides downstream components with a read-only stream of published outcomes.
 */
class DefaultIntelligenceCommandResultConsumptionBoundary @Inject constructor(
    private val publicationBridge: IntelligenceCommandPublicationConsumptionBoundary
) : IntelligenceCommandResultConsumptionBoundary {

    override val resultStream: Flow<IntelligenceApplicationCommandResult> = 
        publicationBridge.consumptionRequestStream.map { it.result }

    override suspend fun consumeResult(operationId: String): IntelligenceApplicationCommandResult? {
        // Step 145: Record/Retrieve consumption of a published result.
        // Delegates to the authoritative stream to ensure consistency.
        return resultStream.firstOrNull { it.operationId == operationId }
    }
}
