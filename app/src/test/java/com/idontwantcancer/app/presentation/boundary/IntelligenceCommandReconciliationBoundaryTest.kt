package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.domain.model.IntelligenceReentryReconciliationConsumptionContract
import com.idontwantcancer.app.presentation.model.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandReconciliationBoundaryTest {

    private val reentryConsumptionBoundary = mockk<IntelligenceReentryReconciliationConsumptionBoundary>()
    private val boundary = DefaultIntelligenceCommandReconciliationBoundary(reentryConsumptionBoundary)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `reentry command reconciliation delegates to domain and confirms success`() = runTest {
        val identity = "sig1::e2"
        val interaction = IntelligenceUiInteraction.RetryOperation
        val contract = IntelligenceReentryReconciliationConsumptionContract(
            reentryIdentity = identity,
            isConsistent = true,
            verifiedAt = Instant.now()
        )
        
        coEvery { reentryConsumptionBoundary.getReconciliationContract(identity) } returns contract

        val result = boundary.reconcile(interaction, identity)

        assertEquals(IntelligenceCommandReconciliationStatus.CONFIRMED_SUCCESS, result.status)
        assertEquals(identity, result.commandIdentity)
    }

    @Test
    fun `reentry command reconciliation confirms failure on inconsistent contract`() = runTest {
        val identity = "sig1::e2"
        val interaction = IntelligenceUiInteraction.RetryOperation
        val contract = IntelligenceReentryReconciliationConsumptionContract(
            reentryIdentity = identity,
            isConsistent = false,
            verifiedAt = Instant.now()
        )
        
        coEvery { reentryConsumptionBoundary.getReconciliationContract(identity) } returns contract

        val result = boundary.reconcile(interaction, identity)

        assertEquals(IntelligenceCommandReconciliationStatus.CONFIRMED_FAILURE, result.status)
    }

    @Test
    fun `generic UI command results in INDETERMINATE status`() = runTest {
        val identity = "load_briefing"
        val interaction = IntelligenceUiInteraction.RetryOperation

        val result = boundary.reconcile(interaction, identity)

        assertEquals(IntelligenceCommandReconciliationStatus.INDETERMINATE, result.status)
    }

    @Test
    fun `missing contract results in INDETERMINATE status`() = runTest {
        val identity = "sig1::e2"
        val interaction = IntelligenceUiInteraction.RetryOperation
        
        coEvery { reentryConsumptionBoundary.getReconciliationContract(identity) } returns null

        val result = boundary.reconcile(interaction, identity)

        assertEquals(IntelligenceCommandReconciliationStatus.INDETERMINATE, result.status)
    }
}
