package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandResultConsumptionBoundaryTest {

    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val handoffBoundary = mockk<IntelligenceCommandResultHandoffBoundary>(relaxed = true)
    private val reentryPublicationBoundary = mockk<com.idontwantcancer.app.domain.engine.IntelligenceReentryCompletionPublicationBoundary>(relaxed = true)
    
    private val consumptionHandoverBridge = DefaultIntelligenceCommandPublicationConsumptionHandoverBoundary()
    
    private val publicationBoundary = DefaultIntelligenceCommandResultPublicationBoundary(
        reentryPublicationBoundary,
        consumptionHandoverBridge
    )
    
    private val publicationBridge = DefaultIntelligenceCommandPublicationConsumptionBoundary(publicationBoundary)
    private val terminalIntegrityBoundary = DefaultIntelligenceCommandTerminalStateIntegrityBoundary()
    private val finalizer = DefaultIntelligenceCommandFinalizationBoundary(lifecycleBoundary, handoffBoundary, terminalIntegrityBoundary)
    private val boundary = DefaultIntelligenceCommandResultConsumptionBoundary(publicationBridge)
    
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `published result is observable through the result stream`() = runTest(testDispatcher) {
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
    fun `unverified result is NOT observable through the result stream`() = runTest(testDispatcher) {
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
        advanceUntilIdle()

        assertEquals(0, collected.size)
        job.cancel()
    }

    @Test
    fun `consumeResult retrieves specific published fact from stream`() = runTest(testDispatcher) {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op3")
        val verification = IntelligenceCommandVerificationResult(
            operationId = "op3",
            status = CommandVerificationStatus.VERIFIED
        )
        val request = IntelligenceCommandPublicationRequest(result, verification)

        val job = launch {
            val consumed = boundary.consumeResult("op3")
            assertEquals(result, consumed)
        }

        publicationBoundary.publishResult(request)
        job.join()
    }
}
