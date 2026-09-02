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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandPublishedResultConsumptionBoundaryTest {

    private val reentryPublicationBoundary = mockk<com.idontwantcancer.app.domain.engine.IntelligenceReentryCompletionPublicationBoundary>(relaxed = true)
    
    private val consumptionHandoverBridge = DefaultIntelligenceCommandPublicationConsumptionHandoverBoundary()

    private val publicationBoundary = DefaultIntelligenceCommandResultPublicationBoundary(
        reentryPublicationBoundary,
        consumptionHandoverBridge
    )
    
    private val publicationBridge = DefaultIntelligenceCommandPublicationConsumptionBoundary(publicationBoundary)
    private val boundary = DefaultIntelligenceCommandResultConsumptionBoundary(publicationBridge)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Test
    fun `published result enters consumption authority exactly once`() = runTest(testDispatcher) {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val verification = IntelligenceCommandVerificationResult(
            operationId = "op1",
            status = CommandVerificationStatus.VERIFIED
        )
        val request = IntelligenceCommandPublicationRequest(result, verification)

        val collected = mutableListOf<IntelligenceApplicationCommandResult>()
        val job = launch {
            boundary.resultStream.take(1).toList(collected)
        }

        publicationBoundary.publishResult(request)

        assertEquals(1, collected.size)
        assertEquals(result, collected[0])
        job.cancel()
    }

    @Test
    fun `unpublished result cannot enter consumption authority`() = runTest(testDispatcher) {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op2")
        val verification = IntelligenceCommandVerificationResult(
            operationId = "op2",
            status = CommandVerificationStatus.UNVERIFIED
        )
        val request = IntelligenceCommandPublicationRequest(result, verification)

        val collected = mutableListOf<IntelligenceApplicationCommandResult>()
        val job = launch {
            boundary.resultStream.collect { collected.add(it) }
        }

        publicationBoundary.publishResult(request)

        assertEquals(0, collected.size)
        job.cancel()
    }
}
