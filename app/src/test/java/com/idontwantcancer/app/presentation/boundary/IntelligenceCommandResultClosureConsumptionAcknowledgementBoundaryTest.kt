package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class IntelligenceCommandResultClosureConsumptionAcknowledgementBoundaryTest {

    private val boundary = DefaultIntelligenceCommandResultClosureConsumptionAcknowledgementBoundary()

    @Test
    fun `acknowledgeConsumption results in a deterministic acknowledgement record`() = runTest {
        val closure = IntelligenceCommandClosureResult(
            operationId = "op1",
            status = CommandResultClosureStatus.CLOSED,
            evaluatedAt = Instant.now()
        )

        val result = boundary.acknowledgeConsumption(closure)

        assertEquals("op1", result.operationId)
        assert(result.reason!!.contains("acknowledged"))
    }
}
