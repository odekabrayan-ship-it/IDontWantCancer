package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.presentation.mapper.toUiState
import com.idontwantcancer.app.presentation.model.ReentryReconciliationUiState
import javax.inject.Inject

/**
 * Default implementation of the projection boundary.
 * Maps authoritative domain contracts to read-only presentation models.
 */
class DefaultReentryStateProjectionBoundary @Inject constructor(
    private val consumptionBoundary: IntelligenceReentryReconciliationConsumptionBoundary
) : ReentryStateProjectionBoundary {

    override suspend fun projectReconciliation(reentryIdentity: String): ReentryReconciliationUiState? {
        return consumptionBoundary.getReconciliationContract(reentryIdentity)?.toUiState()
    }
}
