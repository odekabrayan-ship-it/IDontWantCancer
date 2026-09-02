package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.domain.model.IntelligenceReentryReconciliationConsumptionContract
import kotlinx.coroutines.test.runTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class ReentryStateProjectionBoundaryTest {

    private val consumptionBoundary = mockk<IntelligenceReentryReconciliationConsumptionBoundary>()
    private val projectionBoundary = DefaultReentryStateProjectionBoundary(consumptionBoundary)

    @Test
    fun `consistent state maps to the correct read representation`() = runTest {
        val identity = "sig1::e2"
        val now = Instant.now()
        val contract = IntelligenceReentryReconciliationConsumptionContract(
            reentryIdentity = identity,
            isConsistent = true,
            verifiedAt = now,
            detail = "All good"
        )

        coEvery { consumptionBoundary.getReconciliationContract(identity) } returns contract

        val uiState = projectionBoundary.projectReconciliation(identity)

        assertEquals(identity, uiState?.reentryIdentity)
        assertTrue(uiState?.isConsistent == true)
        assertEquals("All good", uiState?.detail)
    }

    @Test
    fun `inconsistent state maps to the correct read representation`() = runTest {
        val identity = "sig1::e2"
        val contract = IntelligenceReentryReconciliationConsumptionContract(
            reentryIdentity = identity,
            isConsistent = false,
            verifiedAt = Instant.now(),
            detail = "Mismatch"
        )

        coEvery { consumptionBoundary.getReconciliationContract(identity) } returns contract

        val uiState = projectionBoundary.projectReconciliation(identity)

        assertTrue(uiState?.isConsistent == false)
        assertEquals("Mismatch", uiState?.detail)
    }

    @Test
    fun `unknown state remains null`() = runTest {
        val identity = "sig1::e2"
        coEvery { consumptionBoundary.getReconciliationContract(identity) } returns null

        val uiState = projectionBoundary.projectReconciliation(identity)

        assertNull(uiState)
    }
}
