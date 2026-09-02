package com.idontwantcancer.app.presentation.model

/**
 * Presentation-level representation of a re-entry reconciliation outcome.
 * This is a read-only projection of the verified domain contract.
 */
data class ReentryReconciliationUiState(
    val reentryIdentity: String,
    val isConsistent: Boolean,
    val detail: String? = null
)
