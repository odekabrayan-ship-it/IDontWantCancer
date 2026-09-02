package com.idontwantcancer.app.presentation.mapper

import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract
import com.idontwantcancer.app.presentation.model.ReentryReconciliationUiState

/**
 * Maps the Step 107 application read model to the Step 108 presentation contract.
 */

fun ReentryReconciliationUiState?.toContract(): IntelligenceReentryReconciliationPresentationContract {
    return when {
        this == null -> IntelligenceReentryReconciliationPresentationContract.StatusUnavailable
        this.isConsistent -> IntelligenceReentryReconciliationPresentationContract.VerifiedConsistent(reentryIdentity)
        else -> IntelligenceReentryReconciliationPresentationContract.InconsistentMismatch(reentryIdentity, detail)
    }
}
