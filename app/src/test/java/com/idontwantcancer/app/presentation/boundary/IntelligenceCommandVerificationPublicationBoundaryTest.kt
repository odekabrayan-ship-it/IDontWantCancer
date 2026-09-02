package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandVerificationPublicationBoundaryTest {

    private val publicationAuthority = mockk<IntelligenceCommandResultPublicationBoundary>(relaxed = true)
    private val boundary = DefaultIntelligenceCommandVerificationPublicationBoundary(publicationAuthority)

    @Test
    fun `routeToPublication maps handover request to internal publication request and delegates`() = runTest {
        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val verification = IntelligenceCommandVerificationResult(
            operationId = "op1",
            status = CommandVerificationStatus.VERIFIED
        )
        val request = IntelligenceCommandVerificationPublicationHandoverRequest(result, verification)
        
        val publicationResult = IntelligenceCommandPublicationResult(
            operationId = "op1",
            status = CommandPublicationStatus.PUBLISHED,
            publishedAt = Instant.now()
        )
        
        coEvery { publicationAuthority.publishResult(any()) } returns publicationResult

        val actual = boundary.routeToPublication(request)

        assertEquals(publicationResult, actual)
        coVerify { publicationAuthority.publishResult(match { it.result == result && it.verificationResult == verification }) }
    }
}
