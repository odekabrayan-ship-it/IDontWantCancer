package com.idontwantcancer.app.presentation.model

/**
 * Presentation-level representation of command consumption finality.
 * This is a read-only projection of the authoritative finality observation.
 */
data class CommandConsumptionFinalityUiState(
    val operationId: String,
    val isTerminal: Boolean,
    val detail: String? = null
)
