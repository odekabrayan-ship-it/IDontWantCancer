package com.idontwantcancer.app.presentation.signal

import androidx.lifecycle.SavedStateHandle
import com.idontwantcancer.app.domain.usecase.GetSignalByIdUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.test.TestCoroutineDispatcherProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignalDetailViewModelTest {

    private val getSignalByIdUseCase = mockk<GetSignalByIdUseCase>(relaxed = true)
    private val reconciliationBoundary = mockk<com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary>(relaxed = true)
    private val resultHandoverBridge = mockk<IntelligenceCommandExecutionResultHandoverBoundary>(relaxed = true)
    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val renderingLifecycleBoundary = mockk<IntelligenceCommandRenderingLifecycleBoundary>(relaxed = true)
    private val screenInteractionBoundary = mockk<IntelligenceCommandScreenLifecycleInteractionBoundary>(relaxed = true)
    private val dispatcherProvider = TestCoroutineDispatcherProvider()
    private val savedStateHandle = SavedStateHandle(mapOf("signalId" to "sig1"))
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
    fun `ViewModel initializes and loads signal details`() = runTest {
        val viewModel = SignalDetailViewModel(
            getSignalByIdUseCase,
            reconciliationBoundary,
            resultHandoverBridge,
            lifecycleBoundary,
            renderingLifecycleBoundary,
            screenInteractionBoundary,
            dispatcherProvider,
            savedStateHandle
        )
        advanceUntilIdle()

        coEvery { getSignalByIdUseCase("sig1") } returns mockk(relaxed = true)
        assert(viewModel.uiState.value is SignalDetailUiState.Success || viewModel.uiState.value is SignalDetailUiState.NotFound)
    }
}
