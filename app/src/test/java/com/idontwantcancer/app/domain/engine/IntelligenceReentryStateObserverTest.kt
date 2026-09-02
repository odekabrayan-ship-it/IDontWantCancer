package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryStateObserverTest {

    @Test
    fun `authoritative reconciled state is observable`() = runTest {
        val now = Instant.now()
        val result = IntelligenceReentryApplicationStateReconciliationResult(
            reentryIdentity = "sig1::e2",
            status = ApplicationStateReconciliationStatus.CONSISTENT,
            verifiedAt = now
        )
        val handoffBoundary = mockk<IntelligenceReentryReconciliationHandoffBoundary>()
        val flow = MutableSharedFlow<IntelligenceReentryApplicationStateReconciliationResult>(replay = 1)
        flow.emit(result)

        every { handoffBoundary.outcomeStream } returns flow
        
        val observer = DefaultIntelligenceReentryStateObserver(handoffBoundary)

        val observed = observer.reconciliationOutcomes.first()
        assertEquals(result, observed)
    }

    @Test
    fun `observation is deterministic`() = runTest {
        val now = Instant.now()
        val result = createMockResult(now)
        val handoffBoundary = mockk<IntelligenceReentryReconciliationHandoffBoundary>()
        val flow = MutableSharedFlow<IntelligenceReentryApplicationStateReconciliationResult>(replay = 1)
        flow.emit(result)

        every { handoffBoundary.outcomeStream } returns flow
        
        val observer = DefaultIntelligenceReentryStateObserver(handoffBoundary)

        val first = observer.reconciliationOutcomes.first()
        val second = observer.reconciliationOutcomes.first()

        assertEquals(first, second)
    }

    private fun createMockResult(time: Instant) = IntelligenceReentryApplicationStateReconciliationResult(
        reentryIdentity = "sig1::e2",
        status = ApplicationStateReconciliationStatus.CONSISTENT,
        verifiedAt = time
    )
}
