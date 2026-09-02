package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandResultVerificationHandoverBoundaryTest {

    private val verificationAuthority = mockk<IntelligenceCommandResultVerificationBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultVerificationHandoverBoundary(verificationAuthority)

    @Test
    fun `routeToVerification maps result to establishment request and delegates to bridge`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val request = IntelligenceCommandResultVerificationHandoverRequest(result)
        val verificationResult = IntelligenceCommandVerificationResult(
            operationId = "op1",
            status = CommandVerificationStatus.VERIFIED
        )
        
        coEvery { verificationAuthority.routeToVerification(any()) } returns verificationResult

        val actual = boundary.routeToVerification(request)

        assertEquals(verificationResult, actual)
        coVerify { verificationAuthority.routeToVerification(match { it.result == result }) }
    }
}
