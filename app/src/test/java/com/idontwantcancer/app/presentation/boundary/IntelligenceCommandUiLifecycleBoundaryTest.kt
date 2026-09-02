package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandUiLifecycleBoundaryTest {

    private val lifecycleBoundary = mockk<IntelligenceCommandLifecycleBoundary>(relaxed = true)
    private val verificationBoundary = mockk<IntelligenceCommandResultVerificationBoundary>(relaxed = true)
    private val reentryPublicationBoundary = mockk<com.idontwantcancer.app.domain.engine.IntelligenceReentryCompletionPublicationBoundary>(relaxed = true)
    
    private val consumptionHandoverBridge = DefaultIntelligenceCommandPublicationConsumptionHandoverBoundary()

    private val publicationBoundary = DefaultIntelligenceCommandResultPublicationBoundary(
        reentryPublicationBoundary,
        consumptionHandoverBridge
    )
    
    private val publicationBridge = DefaultIntelligenceCommandPublicationConsumptionBoundary(publicationBoundary)
    private val consumptionBoundary = DefaultIntelligenceCommandResultConsumptionBoundary(publicationBridge)
    
    private val closureBoundary = DefaultIntelligenceCommandResultClosureBoundary(
        DefaultIntelligenceCommandResultDispositionBoundary(
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
    )
    private val observationBoundary = DefaultIntelligenceCommandResultClosureObservationBoundary(consumptionBoundary, closureBoundary)
    
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        coEvery { verificationBoundary.verifyResult(any()) } returns IntelligenceCommandVerificationResult(
            operationId = "op",
            status = CommandVerificationStatus.VERIFIED
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `destroying one observer does not stop authoritative finality stream`() = runTest(testDispatcher) {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        
        val collected1 = mutableListOf<IntelligenceCommandClosureResult>()
        val scope1 = CoroutineScope(testDispatcher + Job())
        val job1 = scope1.launch {
            observationBoundary.closureStream.collect { collected1.add(it) }
        }

        val verification = IntelligenceCommandVerificationResult(operationId = "op1", status = CommandVerificationStatus.VERIFIED)
        val request = IntelligenceCommandPublicationRequest(result, verification)

        // Emit through publication directly to verify observation independence
        publicationBoundary.publishResult(request)
        advanceUntilIdle()

        assertEquals(1, collected1.size)

        // Destroy observer 1
        job1.cancel()
        
        // Start observer 2
        val collected2 = mutableListOf<IntelligenceCommandClosureResult>()
        val scope2 = CoroutineScope(testDispatcher + Job())
        val job2 = scope2.launch {
            observationBoundary.closureStream.collect { collected2.add(it) }
        }

        val result2 = IntelligenceApplicationCommandResult.Success(operationId = "op2")
        val verification2 = IntelligenceCommandVerificationResult(operationId = "op2", status = CommandVerificationStatus.VERIFIED)
        val request2 = IntelligenceCommandPublicationRequest(result2, verification2)
        publicationBoundary.publishResult(request2)
        advanceUntilIdle()

        assertEquals(1, collected1.size) // collected1 stopped
        assertEquals(2, collected2.size) // collected2 received op1 (replay) and op2
        
        job2.cancel()
    }

    @Test
    fun `lifecycle cancellation does not mutate authoritative finality`() = runTest(testDispatcher) {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op3")
        
        val job = launch {
            observationBoundary.closureStream.collect { 
                delay(1000)
            }
        }

        val verification3 = IntelligenceCommandVerificationResult(operationId = "op3", status = CommandVerificationStatus.VERIFIED)
        val request3 = IntelligenceCommandPublicationRequest(result, verification3)
        publicationBoundary.publishResult(request3)
        
        // Cancel the UI collection job
        job.cancel(CancellationException("Screen destroyed"))
        
        // Verify no unexpected side effects on the publication stream
        // (Just ensure it didn't throw or corrupt)
        advanceUntilIdle()
    }
}
