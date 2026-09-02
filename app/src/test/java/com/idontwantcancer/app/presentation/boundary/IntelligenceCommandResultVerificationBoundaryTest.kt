package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryPostTransitionVerificationBoundary
import com.idontwantcancer.app.presentation.model.*
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandResultVerificationBoundaryTest {

    private val reentryVerificationBoundary = mockk<IntelligenceReentryPostTransitionVerificationBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultVerificationBoundary(reentryVerificationBoundary)

    @Test
    fun `standard result is verified by default policy`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val request = IntelligenceCommandResultVerificationRequest(result)
        
        val verification = boundary.verifyResult(request)

        assertEquals(CommandVerificationStatus.VERIFIED, verification.status)
        assertEquals("op1", verification.operationId)
    }

    @Test
    fun `reentry success result is verified`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Success(operationId = identity)
        val request = IntelligenceCommandResultVerificationRequest(result)

        val verification = boundary.verifyResult(request)

        assertEquals(CommandVerificationStatus.VERIFIED, verification.status)
    }

    @Test
    fun `reentry failure result is unverified`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Failure("Error", identity)
        val request = IntelligenceCommandResultVerificationRequest(result)

        val verification = boundary.verifyResult(request)

        assertEquals(CommandVerificationStatus.UNVERIFIED, verification.status)
    }
}
