package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandResultVerificationBridgeBoundaryTest {

    private val verificationAuthority = mockk<IntelligenceCommandResultVerificationBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultVerificationBridgeBoundary(verificationAuthority)

    @Test
    fun `routeToVerification maps establishment request to internal verification request and delegates`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val request = IntelligenceCommandResultVerificationHandoverRequest(result)
        val verificationResult = IntelligenceCommandVerificationResult(
            operationId = "op1",
            status = CommandVerificationStatus.VERIFIED
        )
        
        coEvery { verificationAuthority.verifyResult(any()) } returns verificationResult

        val actual = boundary.routeToVerification(request)

        assertEquals(verificationResult, actual)
        coVerify { verificationAuthority.verifyResult(match { it.result == result }) }
    }
}
