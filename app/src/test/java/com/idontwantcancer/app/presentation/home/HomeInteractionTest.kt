package com.idontwantcancer.app.presentation.home

import com.idontwantcancer.app.domain.usecase.GetCurrentBriefingUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.test.TestCoroutineDispatcherProvider
import com.idontwantcancer.app.presentation.model.*
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
class HomeInteractionTest {

    private val getCurrentBriefingUseCase = mockk<GetCurrentBriefingUseCase>(relaxed = true)
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
    fun `RetryOperation interaction triggers briefing reload and formalizes success and lifecycle`() = runTest {
        val viewModel = HomeViewModel(getCurrentBriefingUseCase, resultHandoverBridge, lifecycleBoundary, renderingLifecycleBoundary, screenInteractionBoundary, dispatcherProvider)
        advanceUntilIdle()

        val retryInteraction = IntelligenceUiInteraction.RetryOperation
        viewModel.onInteraction(retryInteraction)
        advanceUntilIdle()

        // once in init, once on interaction
        coVerify(exactly = 2) { getCurrentBriefingUseCase() }
        verify { resultHandoverBridge.routeToResult(retryInteraction, match { it is IntelligenceCommandExecutionOutcome.Success }) }
        verify { lifecycleBoundary.transitionTo(retryInteraction, CommandLifecycleStage.PROCESSING) }
    }
}
