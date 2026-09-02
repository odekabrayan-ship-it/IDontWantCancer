package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandVerificationPublicationHandoverIntegrityTest {

    private val publicationBridge = mockk<IntelligenceCommandVerificationPublicationBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandVerificationPublicationHandoverBoundary(publicationBridge)

    @Test
    fun `handover preserves verification identity and outcome`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op789")
        val verification = IntelligenceCommandVerificationResult(
            operationId = "op789",
            status = CommandVerificationStatus.VERIFIED
        )
        val request = IntelligenceCommandVerificationPublicationHandoverRequest(result, verification)
        
        val publicationResult = IntelligenceCommandPublicationResult(
            operationId = "op789",
            status = CommandPublicationStatus.PUBLISHED
        )
        
        coEvery { publicationBridge.routeToPublication(any()) } returns publicationResult

        val actual = boundary.routeToPublication(request)

        assertEquals(publicationResult, actual)
        coVerify { 
            publicationBridge.routeToPublication(match { 
                it.result == result && it.verificationResult == verification 
            }) 
        }
    }
}
