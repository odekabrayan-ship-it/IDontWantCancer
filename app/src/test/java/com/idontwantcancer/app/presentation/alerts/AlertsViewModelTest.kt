package com.idontwantcancer.app.presentation.alerts

import com.idontwantcancer.app.domain.usecase.GetAttentionSignalsUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.test.TestCoroutineDispatcherProvider
import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlertsViewModelTest {

    private val getAttentionSignalsUseCase = mockk<GetAttentionSignalsUseCase>(relaxed = true)
    private val reconciliationBoundary = mockk<com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary>(relaxed = true)
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
    fun `ViewModel initializes and loads alerts`() = runTest {
        val viewModel = AlertsViewModel(
            getAttentionSignalsUseCase,
            reconciliationBoundary,
            resultHandoverBridge,
            lifecycleBoundary,
            renderingLifecycleBoundary,
            screenInteractionBoundary,
            dispatcherProvider,
            savedStateHandle
        )
        advanceUntilIdle()
        
        coEvery { getAttentionSignalsUseCase() } returns emptyList()
        assert(viewModel.uiState.value is AlertsUiState.Success)
    }
}
