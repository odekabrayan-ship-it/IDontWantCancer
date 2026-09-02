package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryCompletionPublicationBoundary
import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandResultPublicationBoundaryTest {

    private val reentryPublicationBoundary = mockk<IntelligenceReentryCompletionPublicationBoundary>(relaxed = true)
    private val consumptionHandoverBridge = mockk<IntelligenceCommandPublicationConsumptionHandoverBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultPublicationBoundary(reentryPublicationBoundary, consumptionHandoverBridge)

    @Test
    fun `verified result is published to domain pipeline`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Success(operationId = identity)
        val verification = IntelligenceCommandVerificationResult(
            operationId = identity,
            status = CommandVerificationStatus.VERIFIED
        )
        val request = IntelligenceCommandPublicationRequest(result, verification)
        
        val publication = boundary.publishResult(request)

        assertEquals(CommandPublicationStatus.PUBLISHED, publication.status)
        coVerify { reentryPublicationBoundary.publishCompletion(match { it.reentryIdentity == identity }) }
    }

    @Test
    fun `unverified result is suppressed from publication`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Success(operationId = identity)
        val verification = IntelligenceCommandVerificationResult(
            operationId = identity,
            status = CommandVerificationStatus.UNVERIFIED
        )
        val request = IntelligenceCommandPublicationRequest(result, verification)
        
        val publication = boundary.publishResult(request)

        assertEquals(CommandPublicationStatus.SUPPRESSED, publication.status)
        coVerify(exactly = 0) { reentryPublicationBoundary.publishCompletion(any()) }
    }

    @Test
    fun `generic command is published without domain reentry call`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "generic_op")
        val verification = IntelligenceCommandVerificationResult(
            operationId = "generic_op",
            status = CommandVerificationStatus.VERIFIED
        )
        val request = IntelligenceCommandPublicationRequest(result, verification)
        
        val publication = boundary.publishResult(request)

        assertEquals(CommandPublicationStatus.PUBLISHED, publication.status)
        coVerify(exactly = 0) { reentryPublicationBoundary.publishCompletion(any()) }
    }

    @Test
    fun `successful publication emits to consumption stream`() = runTest(UnconfinedTestDispatcher()) {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val verification = IntelligenceCommandVerificationResult(
            operationId = "op1",
            status = CommandVerificationStatus.VERIFIED
        )
        val request = IntelligenceCommandPublicationRequest(result, verification)

        val collected = mutableListOf<IntelligenceCommandConsumptionRequest>()
        val consumptionRequest = IntelligenceCommandConsumptionRequest(result, mockk())
        every { consumptionHandoverBridge.routeToConsumption(any()) } returns consumptionRequest
        
        val job = launch {
            boundary.consumptionStream.take(1).toList(collected)
        }

        boundary.publishResult(request)

        assertEquals(1, collected.size)
        assertEquals(result, collected[0].result)
        job.cancel()
    }
}
