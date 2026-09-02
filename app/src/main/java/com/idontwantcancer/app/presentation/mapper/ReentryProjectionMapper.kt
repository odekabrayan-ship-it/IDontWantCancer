package com.idontwantcancer.app.presentation.mapper

import com.idontwantcancer.app.domain.model.IntelligenceReentryReconciliationConsumptionContract
import com.idontwantcancer.app.presentation.model.ReentryReconciliationUiState

/**
 * Maps re-entry domain contracts to application-level read projections.
 */

fun IntelligenceReentryReconciliationConsumptionContract.toUiState(): ReentryReconciliationUiState {
    return ReentryReconciliationUiState(
        reentryIdentity = reentryIdentity,
        isConsistent = isConsistent,
        detail = detail
    )
}
