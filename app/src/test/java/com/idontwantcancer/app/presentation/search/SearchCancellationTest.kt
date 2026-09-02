package com.idontwantcancer.app.presentation.search

import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.domain.usecase.SearchSignalsUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.test.TestCoroutineDispatcherProvider
import com.idontwantcancer.app.presentation.model.*
import androidx.lifecycle.SavedStateHandle
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchCancellationTest {

    private val searchSignalsUseCase = mockk<SearchSignalsUseCase>()
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
    fun `CancelOperation interaction stops search and reports CANCELLED stage`() = runTest {
        val viewModel = SearchViewModel(searchSignalsUseCase, reconciliationBoundary, resultHandoverBridge, lifecycleBoundary, renderingLifecycleBoundary, screenInteractionBoundary, dispatcherProvider, savedStateHandle)
        val query = "test"
        
        coEvery { searchSignalsUseCase(query) } coAnswers {
            delay(1000)
            emptyList()
        }

        val searchInteraction = IntelligenceUiInteraction.PerformSearch(query)
        viewModel.onInteraction(searchInteraction)
        advanceTimeBy(500)

        // Cancel the search
        viewModel.onInteraction(IntelligenceUiInteraction.CancelOperation(searchInteraction))
        advanceUntilIdle()

        verify { resultHandoverBridge.routeToResult(searchInteraction, match { it is IntelligenceCommandExecutionOutcome.Cancelled }) }
    }
}
