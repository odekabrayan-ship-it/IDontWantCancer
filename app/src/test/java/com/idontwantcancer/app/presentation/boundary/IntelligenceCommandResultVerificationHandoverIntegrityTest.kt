package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandResultVerificationHandoverIntegrityTest {

    private val verificationBridge = mockk<IntelligenceCommandResultVerificationBridgeBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandResultVerificationHandoverBoundary(verificationBridge)

    @Test
    fun `handover preserves result identity and outcome`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op123")
        val verificationResult = IntelligenceCommandVerificationResult(
            operationId = "op123",
            status = CommandVerificationStatus.VERIFIED
        )
        
        coEvery { verificationBridge.routeToVerification(any()) } returns verificationResult

        val actual = boundary.routeToVerification(IntelligenceCommandResultVerificationHandoverRequest(result))

        assertEquals(verificationResult, actual)
        coVerify { 
            verificationBridge.routeToVerification(match { 
                it.result == result && it.result.operationId == "op123" 
            }) 
        }
    }

    @Test
    fun `handover preserves failure outcome integrity`() = runTest {
        val result = IntelligenceApplicationCommandResult.Failure("Critical Error", "op456")
        
        boundary.routeToVerification(IntelligenceCommandResultVerificationHandoverRequest(result))

        coVerify { 
            verificationBridge.routeToVerification(match { 
                it.result is IntelligenceApplicationCommandResult.Failure && 
                (it.result as IntelligenceApplicationCommandResult.Failure).reason == "Critical Error"
            }) 
        }
    }
}
