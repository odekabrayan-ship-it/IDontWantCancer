package com.idontwantcancer.app.presentation.mapper

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CommandConsumptionFinalityPresentationMapperTest {

    @Test
    fun `null projection maps to StatusUnavailable`() {
        val uiState: CommandConsumptionFinalityUiState? = null
        val contract = uiState.toContract()
        assertTrue(contract is CommandConsumptionFinalityPresentationContract.StatusUnavailable)
    }

    @Test
    fun `terminal projection maps to Final`() {
        val uiState = CommandConsumptionFinalityUiState(
            operationId = "op1",
            isTerminal = true,
            detail = "Done"
        )
        val contract = uiState.toContract()
        assertTrue(contract is CommandConsumptionFinalityPresentationContract.Final)
        assertEquals("op1", contract.operationId)
        assertEquals("Done", (contract as CommandConsumptionFinalityPresentationContract.Final).detail)
    }

    @Test
    fun `non-terminal projection maps to NonTerminal`() {
        val uiState = CommandConsumptionFinalityUiState(
            operationId = "op2",
            isTerminal = false,
            detail = "Processing"
        )
        val contract = uiState.toContract()
        assertTrue(contract is CommandConsumptionFinalityPresentationContract.NonTerminal)
        assertEquals("op2", contract.operationId)
        assertEquals("Processing", (contract as CommandConsumptionFinalityPresentationContract.NonTerminal).detail)
    }
}
