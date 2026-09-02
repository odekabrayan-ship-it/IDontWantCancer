package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryAcknowledgementBoundary
import com.idontwantcancer.app.presentation.model.*
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultAcknowledgementBoundaryTest {

    private val reentryAcknowledgementBoundary = mockk<IntelligenceReentryAcknowledgementBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultAcknowledgementBoundary(reentryAcknowledgementBoundary)

    @Test
    fun `generic result results in standard ACKNOWLEDGED status`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "generic_op")
        val request = IntelligenceCommandAcknowledgementRequest(result)
        
        val ack = boundary.acknowledge(request)

        assertEquals(CommandAcknowledgementStatus.ACKNOWLEDGED, ack.status)
        assertEquals("generic_op", ack.operationId)
    }

    @Test
    fun `reentry result delegates to domain authority and returns ACKNOWLEDGED`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Failure("Error", identity)
        val request = IntelligenceCommandAcknowledgementRequest(result)

        val ack = boundary.acknowledge(request)

        assertEquals(CommandAcknowledgementStatus.ACKNOWLEDGED, ack.status)
        coVerify { 
            reentryAcknowledgementBoundary.acknowledge(match { 
                it.reentryIdentity == identity && 
                it.reason!!.contains("Step 124") 
            }) 
        }
    }
}
