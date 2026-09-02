package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceReentryReconciliationConsumptionBoundaryTest {

    private val handoffBoundary = mockk<IntelligenceReentryReconciliationHandoffBoundary>()
    private val boundary = DefaultIntelligenceReentryReconciliationConsumptionBoundary(handoffBoundary)

    @Test
    fun `consumer can consume CONSISTENT state`() = runTest {
        val identity = "sig1::e2"
        val now = Instant.now()
        val result = IntelligenceReentryApplicationStateReconciliationResult(
            reentryIdentity = identity,
            status = ApplicationStateReconciliationStatus.CONSISTENT,
            verifiedAt = now
        )

        every { handoffBoundary.getLatestResult(identity) } returns result

        val contract = boundary.getReconciliationContract(identity)

        assertTrue(contract?.isConsistent == true)
        assertEquals(identity, contract?.reentryIdentity)
    }

    @Test
    fun `consumer can distinguish INCONSISTENT state`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceReentryApplicationStateReconciliationResult(
            reentryIdentity = identity,
            status = ApplicationStateReconciliationStatus.INCONSISTENT,
            details = "Error",
            verifiedAt = Instant.now()
        )

        every { handoffBoundary.getLatestResult(identity) } returns result

        val contract = boundary.getReconciliationContract(identity)

        assertTrue(contract?.isConsistent == false)
        assertEquals("Error", contract?.detail)
    }

    @Test
    fun `non-existent result returns null contract`() = runTest {
        val identity = "sig1::e2"
        every { handoffBoundary.getLatestResult(identity) } returns null

        val contract = boundary.getReconciliationContract(identity)

        assertNull(contract)
    }
}
