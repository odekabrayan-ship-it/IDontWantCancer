package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationHandoffBoundary
import com.idontwantcancer.app.domain.model.ApplicationStateReconciliationStatus
import com.idontwantcancer.app.presentation.model.*
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandResultHandoffBoundaryTest {

    private val reentryHandoffBoundary = mockk<IntelligenceReentryReconciliationHandoffBoundary>(relaxed = true)
    private val acceptanceBoundary = mockk<IntelligenceCommandResultAcceptanceBoundary>(relaxed = true)
    private val rejectionBoundary = mockk<IntelligenceCommandResultRejectionBoundary>(relaxed = true)
    private val dispositionBoundary = mockk<IntelligenceCommandResultDispositionBoundary>(relaxed = true)
    private val closureBoundary = mockk<IntelligenceCommandResultClosureBoundary>(relaxed = true)
    private val verificationBridge = mockk<IntelligenceCommandResultVerificationHandoverBoundary>(relaxed = true)
    private val publicationBridge = mockk<IntelligenceCommandVerificationPublicationHandoverBoundary>(relaxed = true)
    private val consumptionBridge = mockk<IntelligenceCommandPublicationConsumptionHandoverBoundary>(relaxed = true)
    private val acknowledgementHandoverBridge = mockk<IntelligenceCommandConsumptionAcknowledgementHandoverBoundary>(relaxed = true)
    private val closureHandoverBridge = mockk<IntelligenceCommandAcknowledgementClosureHandoverBoundary>(relaxed = true)
    
    private val boundary = DefaultIntelligenceCommandResultHandoffBoundary(
        reentryHandoffBoundary, 
        acceptanceBoundary, 
        rejectionBoundary, 
        dispositionBoundary, 
        closureBoundary, 
        verificationBridge, 
        publicationBridge, 
        consumptionBridge, 
        acknowledgementHandoverBridge,
        closureHandoverBridge
    )
    
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
    fun `reentry success result is handed off to domain as CONSISTENT`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Success(operationId = identity)

        boundary.performHandoff(result)

        coVerify { 
            reentryHandoffBoundary.handoffResult(match { 
                it.reentryIdentity == identity && 
                it.status == ApplicationStateReconciliationStatus.CONSISTENT 
            }) 
        }
    }

    @Test
    fun `reentry failure result is handed off to domain as INCONSISTENT`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Failure("Technical error", identity)

        boundary.performHandoff(result)

        coVerify { 
            reentryHandoffBoundary.handoffResult(match { 
                it.reentryIdentity == identity && 
                it.status == ApplicationStateReconciliationStatus.INCONSISTENT &&
                it.details!!.contains("Technical error")
            }) 
        }
    }

    @Test
    fun `generic command result is not handed off to reentry boundary but is acknowledged and accepted`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "generic_op")
        coEvery { acceptanceBoundary.evaluateAcceptance(result) } returns IntelligenceCommandAcceptanceResult(
            operationId = "generic_op",
            status = CommandAcceptanceStatus.ACCEPTED,
            evaluatedAt = java.time.Instant.now()
        )

        boundary.performHandoff(result)

        coVerify(exactly = 0) { reentryHandoffBoundary.handoffResult(any()) }
        coVerify { acknowledgementHandoverBridge.routeToAcknowledgement(any()) }
        coVerify { acceptanceBoundary.evaluateAcceptance(result) }
        coVerify(exactly = 0) { rejectionBoundary.recordRejection(any(), any()) }
        coVerify { dispositionBoundary.evaluateDisposition(result) }
        coVerify { closureHandoverBridge.routeToClosure(any()) }
        coVerify { verificationBridge.routeToVerification(any()) }
        coVerify { publicationBridge.routeToPublication(any()) }
    }

    @Test
    fun `rejected command result triggers rejection boundary`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "generic_op")
        coEvery { acceptanceBoundary.evaluateAcceptance(result) } returns IntelligenceCommandAcceptanceResult(
            operationId = "generic_op",
            status = CommandAcceptanceStatus.REJECTED,
            reason = "Contract mismatch",
            evaluatedAt = java.time.Instant.now()
        )

        boundary.performHandoff(result)

        coVerify { rejectionBoundary.recordRejection(result, "Contract mismatch") }
        coVerify { dispositionBoundary.evaluateDisposition(result) }
        coVerify { closureHandoverBridge.routeToClosure(any()) }
    }
}
