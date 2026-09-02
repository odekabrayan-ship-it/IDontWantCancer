package com.idontwantcancer.app.presentation.search

import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.domain.usecase.SearchSignalsUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.test.TestCoroutineDispatcherProvider
import com.idontwantcancer.app.presentation.model.*
import androidx.lifecycle.SavedStateHandle
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchInteractionTest {

    private val searchSignalsUseCase = mockk<SearchSignalsUseCase>(relaxed = true)
    private val reconciliationBoundary = mockk<IntelligenceReentryReconciliationConsumptionBoundary>(relaxed = true)
    private val resultHandoverBridge = mockk<IntelligenceCommandExecutionResultHandoverBoundary>(relaxed = true)
    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val renderingLifecycleBoundary = mockk<IntelligenceCommandRenderingLifecycleBoundary>(relaxed = true)
    private val screenInteractionBoundary = mockk<IntelligenceCommandScreenLifecycleInteractionBoundary>(relaxed = true)
    private val dispatcherProvider = TestCoroutineDispatcherProvider()
    private val savedStateHandle = SavedStateHandle()
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
    fun `PerformSearch interaction triggers search use case and formalizes result and lifecycle`() = runTest {
        val viewModel = SearchViewModel(searchSignalsUseCase, reconciliationBoundary, resultHandoverBridge, lifecycleBoundary, renderingLifecycleBoundary, screenInteractionBoundary, dispatcherProvider, savedStateHandle)
        
        val interaction = IntelligenceUiInteraction.PerformSearch("test")
        viewModel.onInteraction(interaction)
        advanceUntilIdle()

        coVerify { searchSignalsUseCase("test") }
        verify { resultHandoverBridge.routeToResult(interaction, match { it is IntelligenceCommandExecutionOutcome.Success }) }
        verify { lifecycleBoundary.transitionTo(interaction, CommandLifecycleStage.PROCESSING) }
    }

    @Test
    fun `ClearSearch interaction resets UI state and completes lifecycle`() = runTest {
        val viewModel = SearchViewModel(searchSignalsUseCase, reconciliationBoundary, resultHandoverBridge, lifecycleBoundary, renderingLifecycleBoundary, screenInteractionBoundary, dispatcherProvider, savedStateHandle)
        
        val interaction = IntelligenceUiInteraction.ClearSearch
        viewModel.onInteraction(interaction)
        advanceUntilIdle()

        assert(viewModel.uiState.value is SearchUiState.Idle)
        verify { resultHandoverBridge.routeToResult(interaction, match { it is IntelligenceCommandExecutionOutcome.Success }) }
    }
}
