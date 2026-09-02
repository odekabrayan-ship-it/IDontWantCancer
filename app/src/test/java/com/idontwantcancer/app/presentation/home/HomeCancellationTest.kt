package com.idontwantcancer.app.presentation.home

import com.idontwantcancer.app.domain.usecase.GetCurrentBriefingUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.test.TestCoroutineDispatcherProvider
import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeCancellationTest {

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
    fun `CancelOperation interaction stops load and reports CANCELLED stage`() = runTest {
        // Init calls loadBriefing()
        coEvery { getCurrentBriefingUseCase() } coAnswers {
            delay(1000)
            mockk(relaxed = true)
        }

        val viewModel = HomeViewModel(getCurrentBriefingUseCase, resultHandoverBridge, lifecycleBoundary, renderingLifecycleBoundary, screenInteractionBoundary, dispatcherProvider)
        advanceTimeBy(500)

        val retryInteraction = IntelligenceUiInteraction.RetryOperation
        viewModel.onInteraction(retryInteraction)
        advanceTimeBy(100)

        // Cancel the retry
        viewModel.onInteraction(IntelligenceUiInteraction.CancelOperation(retryInteraction))
        advanceUntilIdle()

        verify { resultHandoverBridge.routeToResult(retryInteraction, match { it is IntelligenceCommandExecutionOutcome.Cancelled }) }
    }
}
