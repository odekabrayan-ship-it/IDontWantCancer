package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommandConsumptionFinalityPresentationContractBoundaryTest {

    private val renderingHandoverBridge = mockk<IntelligenceCommandPresentationRenderingHandoverBoundary>(relaxed = true)
    private lateinit var boundary: CommandConsumptionFinalityPresentationContractBoundary

    @Before
    fun setup() {
        boundary = DefaultCommandConsumptionFinalityPresentationContractBoundary(renderingHandoverBridge)
    }

    @Test
    fun `terminal projection outcome maps to Final contract`() = runTest(UnconfinedTestDispatcher()) {
        val collected = mutableListOf<CommandConsumptionFinalityPresentationContract>()
        val job = launch {
            boundary.presentationStream.toList(collected)
        }

        val projection = CommandConsumptionFinalityUiState(operationId = "op1", isTerminal = true)
        boundary.presentProjection(IntelligenceCommandProjectionPresentationHandoverRequest(projection))

        assertEquals(1, collected.size)
        assertTrue(collected[0] is CommandConsumptionFinalityPresentationContract.Final)
        assertEquals("op1", collected[0].operationId)
        val expectedHandover = IntelligenceCommandPresentationRenderingHandoverRequest(collected[0])
        verify { renderingHandoverBridge.routeToRendering(expectedHandover) }
        job.cancel()
    }

    @Test
    fun `non-terminal projection maps to NonTerminal contract`() = runTest(UnconfinedTestDispatcher()) {
        val collected = mutableListOf<CommandConsumptionFinalityPresentationContract>()
        val job = launch {
            boundary.presentationStream.toList(collected)
        }

        val projection = CommandConsumptionFinalityUiState(operationId = "op2", isTerminal = false)
        boundary.presentProjection(IntelligenceCommandProjectionPresentationHandoverRequest(projection))

        assertEquals(1, collected.size)
        assertTrue(collected[0] is CommandConsumptionFinalityPresentationContract.NonTerminal)
        assertEquals("op2", collected[0].operationId)
        job.cancel()
    }
}
