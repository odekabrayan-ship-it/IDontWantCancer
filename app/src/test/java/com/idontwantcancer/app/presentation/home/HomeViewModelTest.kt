package com.idontwantcancer.app.presentation.home

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.usecase.GetCurrentBriefingUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.test.TestCoroutineDispatcherProvider
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getCurrentBriefingUseCase = mockk<GetCurrentBriefingUseCase>()
    private val resultHandoverBridge = mockk<IntelligenceCommandExecutionResultHandoverBoundary>(relaxed = true)
    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val renderingLifecycleBoundary = mockk<IntelligenceCommandRenderingLifecycleBoundary>(relaxed = true)
    private val screenInteractionBoundary = mockk<IntelligenceCommandScreenLifecycleInteractionBoundary>(relaxed = true)
    private val dispatcherProvider = TestCoroutineDispatcherProvider()
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
    fun `loadBriefing maps domain reconciliation to presentation contract`() = runTest {
        val now = Instant.now()
        val briefing = createMockBriefing(now)
        coEvery { getCurrentBriefingUseCase() } returns briefing

        val viewModel = HomeViewModel(getCurrentBriefingUseCase, resultHandoverBridge, lifecycleBoundary, renderingLifecycleBoundary, screenInteractionBoundary, dispatcherProvider)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is HomeUiState.Success)
        val success = state as HomeUiState.Success
        
        // sig1 has a consistent contract
        assertTrue(success.reconciliations["sig1"] is IntelligenceReentryReconciliationPresentationContract.VerifiedConsistent)
    }

    private fun createMockBriefing(time: Instant) = IntelligenceBriefing(
        id = "b1",
        cycleId = "c1",
        generatedAt = time,
        status = BriefingStatus.READY,
        items = listOf(
            IntelligenceBriefingItem(
                id = "item1",
                position = 0,
                communicationPackageId = "p1",
                intelligenceId = "sig1",
                threadId = "t1",
                currentStateReference = "e2",
                changeReference = null,
                continuityReference = null,
                significanceReference = SignificanceOutcome.SIGNIFICANT,
                priorityReference = AttentionLevel.ROUTINE,
                evidenceReference = EvidenceSynthesisLevel.SUPPORTED,
                uncertaintyReference = SignalConfidence.HIGH,
                conflictReference = false,
                narrativeReference = "t1",
                provenanceReference = "p1",
                inclusionReason = "R",
                explanation = null,
                readiness = CommunicationReadinessLevel.READY,
                reentryContract = null,
                reconciliationContract = IntelligenceReentryReconciliationConsumptionContract(
                    reentryIdentity = "sig1::e2",
                    isConsistent = true,
                    verifiedAt = time
                )
            )
        ),
        signals = mapOf("sig1" to mockk(relaxed = true)),
        handoffs = emptyMap()
    )
}
