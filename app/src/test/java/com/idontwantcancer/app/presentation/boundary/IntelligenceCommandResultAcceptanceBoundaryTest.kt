package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryAcknowledgementBoundary
import com.idontwantcancer.app.presentation.model.*
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandResultAcceptanceBoundaryTest {

    private val reentryAcknowledgementBoundary = mockk<IntelligenceReentryAcknowledgementBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultAcceptanceBoundary(reentryAcknowledgementBoundary)

    @Test
    fun `generic result results in standard ACCEPTED status`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "generic_op")
        
        val acceptance = boundary.evaluateAcceptance(result)

        assertEquals(CommandAcceptanceStatus.ACCEPTED, acceptance.status)
        assertEquals("generic_op", acceptance.operationId)
    }

    @Test
    fun `reentry result is accepted based on domain receiving contract`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Success(operationId = identity)

        val acceptance = boundary.evaluateAcceptance(result)

        assertEquals(CommandAcceptanceStatus.ACCEPTED, acceptance.status)
    }
}
