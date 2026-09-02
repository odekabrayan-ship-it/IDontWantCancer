package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryAcknowledgementBoundary
import com.idontwantcancer.app.presentation.model.*
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultRejectionBoundaryTest {

    private val reentryAcknowledgementBoundary = mockk<IntelligenceReentryAcknowledgementBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultRejectionBoundary(reentryAcknowledgementBoundary)

    @Test
    fun `generic result rejection is recorded and does not mutate command result`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "generic_op")
        
        val rejection = boundary.recordRejection(result, "UI Contract Mismatch")

        assertEquals("generic_op", rejection.operationId)
        assertEquals("UI Contract Mismatch", rejection.reason)
        // Command result is Success, rejection is just a receipt fact
    }

    @Test
    fun `reentry result rejection delegates to Step 91 authority`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Success(operationId = identity)

        val rejection = boundary.recordRejection(result, "Invalid Domain Payload")

        assertEquals(identity, rejection.operationId)
        coVerify { 
            reentryAcknowledgementBoundary.acknowledge(match { 
                it.reentryIdentity == identity && 
                it.status == com.idontwantcancer.app.domain.model.ReentryAcknowledgementStatus.REJECTED &&
                it.reason!!.contains("Invalid Domain Payload")
            }) 
        }
    }
}
