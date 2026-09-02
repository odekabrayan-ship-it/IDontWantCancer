package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultDispositionBoundaryTest {

    private val acknowledgementHandoverBridge = mockk<IntelligenceCommandConsumptionAcknowledgementHandoverBoundary>()
    private val acceptanceBoundary = mockk<IntelligenceCommandResultAcceptanceBoundary>()
    private val boundary = DefaultIntelligenceCommandResultDispositionBoundary(acknowledgementHandoverBridge, acceptanceBoundary)

    @Test
    fun `ACCEPTED disposition is returned when acceptance status is ACCEPTED`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        
        coEvery { acknowledgementHandoverBridge.routeToAcknowledgement(any()) } returns IntelligenceCommandAcknowledgementResult(
            operationId = "op1",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED
        )
        coEvery { acceptanceBoundary.evaluateAcceptance(result) } returns IntelligenceCommandAcceptanceResult(
            operationId = "op1",
            status = CommandAcceptanceStatus.ACCEPTED
        )

        val dispositionResult = boundary.evaluateDisposition(result)

        assertEquals(CommandResultDisposition.ACCEPTED, dispositionResult.disposition)
    }

    @Test
    fun `REJECTED disposition is returned when acceptance status is REJECTED`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        
        coEvery { acknowledgementHandoverBridge.routeToAcknowledgement(any()) } returns IntelligenceCommandAcknowledgementResult(
            operationId = "op1",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED
        )
        coEvery { acceptanceBoundary.evaluateAcceptance(result) } returns IntelligenceCommandAcceptanceResult(
            operationId = "op1",
            status = CommandAcceptanceStatus.REJECTED
        )

        val dispositionResult = boundary.evaluateDisposition(result)

        assertEquals(CommandResultDisposition.REJECTED, dispositionResult.disposition)
    }

    @Test
    fun `ACKNOWLEDGED disposition is returned when only acknowledged`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        
        coEvery { acknowledgementHandoverBridge.routeToAcknowledgement(any()) } returns IntelligenceCommandAcknowledgementResult(
            operationId = "op1",
            status = CommandAcknowledgementStatus.ACKNOWLEDGED
        )
        coEvery { acceptanceBoundary.evaluateAcceptance(result) } returns IntelligenceCommandAcceptanceResult(
            operationId = "op1",
            status = CommandAcceptanceStatus.PENDING
        )

        val dispositionResult = boundary.evaluateDisposition(result)

        assertEquals(CommandResultDisposition.ACKNOWLEDGED, dispositionResult.disposition)
    }

    @Test
    fun `UNRESOLVED disposition is returned when neither ack nor acceptance established`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        
        coEvery { acknowledgementHandoverBridge.routeToAcknowledgement(any()) } returns IntelligenceCommandAcknowledgementResult(
            operationId = "op1",
            status = CommandAcknowledgementStatus.PENDING
        )
        coEvery { acceptanceBoundary.evaluateAcceptance(result) } returns IntelligenceCommandAcceptanceResult(
            operationId = "op1",
            status = CommandAcceptanceStatus.PENDING
        )

        val dispositionResult = boundary.evaluateDisposition(result)

        assertEquals(CommandResultDisposition.UNRESOLVED, dispositionResult.disposition)
    }
}
