package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import io.mockk.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandPublicationConsumptionBoundaryTest {

    private val publicationAuthority = mockk<IntelligenceCommandResultPublicationBoundary>()

    @Test
    fun `consumption stream provides formalized requests from publication authority`() = runTest(UnconfinedTestDispatcher()) {
        val stream = MutableSharedFlow<IntelligenceCommandConsumptionRequest>()
        every { publicationAuthority.consumptionStream } returns stream
        
        val boundary = DefaultIntelligenceCommandPublicationConsumptionBoundary(publicationAuthority)

        val result = IntelligenceApplicationCommandResult.Success(operationId = "op1")
        val publicationResult = IntelligenceCommandPublicationResult(
            operationId = "op1",
            status = CommandPublicationStatus.PUBLISHED,
            publishedAt = Instant.now()
        )
        val request = IntelligenceCommandConsumptionRequest(result, publicationResult)

        val collected = mutableListOf<IntelligenceCommandConsumptionRequest>()
        val job = launch {
            boundary.consumptionRequestStream.take(1).toList(collected)
        }

        stream.emit(request)

        assertEquals(1, collected.size)
        assertEquals(request, collected[0])
        job.cancel()
    }
}
