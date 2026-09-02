package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IntelligenceCommandVerificationPublicationHandoverBoundaryTest {

    private val publicationAuthority = mockk<IntelligenceCommandVerificationPublicationBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandVerificationPublicationHandoverBoundary(publicationAuthority)

    @Test
    fun `routeToPublication maps verification outcome to handover request and delegates to bridge`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val verification = IntelligenceCommandVerificationResult(
            operationId = "op1",
            status = CommandVerificationStatus.VERIFIED
        )
        val request = IntelligenceCommandVerificationPublicationHandoverRequest(result, verification)
        
        val publicationResult = IntelligenceCommandPublicationResult(
            operationId = "op1",
            status = CommandPublicationStatus.PUBLISHED
        )
        
        coEvery { publicationAuthority.routeToPublication(any()) } returns publicationResult

        val actual = boundary.routeToPublication(request)

        assertEquals(publicationResult, actual)
        coVerify { publicationAuthority.routeToPublication(match { it.result == result && it.verificationResult == verification }) }
    }
}
