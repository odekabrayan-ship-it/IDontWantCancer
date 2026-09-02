package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryReconciliationHandoffBoundaryTest {

    private val boundary = DefaultIntelligenceReentryReconciliationHandoffBoundary()

    @Test
    fun `CONSISTENT result can be handed off and observed`() = runTest {
        val result = IntelligenceReentryApplicationStateReconciliationResult(
            reentryIdentity = "sig1::e2",
            status = ApplicationStateReconciliationStatus.CONSISTENT,
            verifiedAt = Instant.now()
        )

        boundary.handoffResult(result)

        val observed = boundary.outcomeStream.first()
        assertEquals(result, observed)
    }

    @Test
    fun `INCONSISTENT result can be handed off and observed`() = runTest {
        val result = IntelligenceReentryApplicationStateReconciliationResult(
            reentryIdentity = "sig1::e2",
            status = ApplicationStateReconciliationStatus.INCONSISTENT,
            details = "Signal mismatch",
            verifiedAt = Instant.now()
        )

        boundary.handoffResult(result)

        val observed = boundary.outcomeStream.first()
        assertEquals(result, observed)
    }

    @Test
    fun `handoff is deterministic`() = runTest {
        val result = IntelligenceReentryApplicationStateReconciliationResult(
            reentryIdentity = "sig1::e2",
            status = ApplicationStateReconciliationStatus.CONSISTENT,
            verifiedAt = Instant.now()
        )

        boundary.handoffResult(result)
        val first = boundary.outcomeStream.first()
        
        boundary.handoffResult(result)
        val second = boundary.outcomeStream.first()

        assertEquals(first, second)
    }
}
