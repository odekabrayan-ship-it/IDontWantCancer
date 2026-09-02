package com.idontwantcancer.app.presentation.mapper

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinality
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityUiState
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult

/**
 * Maps authoritative finality results to read-only presentation models.
 */
fun IntelligenceCommandConsumptionFinalityResult.toUiState(): CommandConsumptionFinalityUiState {
    return CommandConsumptionFinalityUiState(
        operationId = operationId,
        isTerminal = finality == CommandConsumptionFinality.TERMINAL,
        detail = reason
    )
}
