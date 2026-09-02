package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultIntelligenceCommandRenderingLifecycleBoundaryTest {

    private val boundary = DefaultIntelligenceCommandRenderingLifecycleBoundary()

    @Test
    fun `participate emits contract to lifecycle rendering stream`() = runTest(UnconfinedTestDispatcher()) {
        val contract = CommandConsumptionFinalityPresentationContract.Final(
            operationId = "op1",
            detail = "Done"
        )
        val request = IntelligenceCommandRenderingLifecycleHandoverRequest(contract)

        val collected = mutableListOf<CommandConsumptionFinalityPresentationContract>()
        val job = launch {
            boundary.lifecycleRenderingStream.take(1).toList(collected)
        }

        boundary.participate(request)

        assertEquals(1, collected.size)
        assertEquals(contract, collected[0])
        job.cancel()
    }
}
