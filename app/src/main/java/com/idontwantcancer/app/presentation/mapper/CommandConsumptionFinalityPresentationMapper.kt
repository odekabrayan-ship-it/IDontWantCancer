package com.idontwantcancer.app.presentation.mapper

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityUiState

/**
 * Maps finality projections to presentation-safe contracts.
 */
fun CommandConsumptionFinalityUiState?.toContract(): CommandConsumptionFinalityPresentationContract {
    return when {
        this == null -> CommandConsumptionFinalityPresentationContract.StatusUnavailable
        this.isTerminal -> CommandConsumptionFinalityPresentationContract.Final(
            operationId = operationId,
            detail = detail
        )
        else -> CommandConsumptionFinalityPresentationContract.NonTerminal(
            operationId = operationId,
            detail = detail
        )
    }
}
