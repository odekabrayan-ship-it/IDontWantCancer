package com.idontwantcancer.app.presentation.mapper

import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract
import com.idontwantcancer.app.presentation.model.ReentryReconciliationUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReentryPresentationMapperTest {

    @Test
    fun `consistent read model maps to VerifiedConsistent contract`() {
        val readModel = ReentryReconciliationUiState(
            reentryIdentity = "sig1::e2",
            isConsistent = true,
            detail = "All good"
        )

        val contract = readModel.toContract()

        assertTrue(contract is IntelligenceReentryReconciliationPresentationContract.VerifiedConsistent)
        assertEquals("sig1::e2", contract.reentryIdentity)
    }

    @Test
    fun `inconsistent read model maps to InconsistentMismatch contract`() {
        val readModel = ReentryReconciliationUiState(
            reentryIdentity = "sig1::e2",
            isConsistent = false,
            detail = "Mismatch"
        )

        val contract = readModel.toContract()

        assertTrue(contract is IntelligenceReentryReconciliationPresentationContract.InconsistentMismatch)
        assertEquals("Mismatch", (contract as IntelligenceReentryReconciliationPresentationContract.InconsistentMismatch).details)
    }

    @Test
    fun `null read model maps to StatusUnavailable contract`() {
        val readModel: ReentryReconciliationUiState? = null

        val contract = readModel.toContract()

        assertTrue(contract is IntelligenceReentryReconciliationPresentationContract.StatusUnavailable)
    }
}
