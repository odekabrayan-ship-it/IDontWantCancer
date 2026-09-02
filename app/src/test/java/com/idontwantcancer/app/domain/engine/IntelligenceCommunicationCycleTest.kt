package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationCycleTest {

    @Test
    fun `cycle preserves order and identity of selected handoffs`() {
        val h1 = createMockHandoff("sig1")
        val h2 = createMockHandoff("sig2")
        val timestamp = Instant.ofEpochMilli(123456789)
        val cycleId = "cycle-123456789"
        
        val cycle = IntelligenceCommunicationCycle(
            id = cycleId,
            timestamp = timestamp,
            selectedHandoffs = listOf(h1, h2),
            status = BriefingStatus.READY
        )

        assertEquals(cycleId, cycle.id)
        assertEquals(2, cycle.selectedHandoffs.size)
        assertEquals("sig1", cycle.selectedHandoffs[0].intelligenceId)
        assertEquals("sig2", cycle.selectedHandoffs[1].intelligenceId)
    }

    @Test
    fun `empty selection produces a valid empty cycle`() {
        val timestamp = Instant.now()
        val cycle = IntelligenceCommunicationCycle(
            id = "cycle-empty",
            timestamp = timestamp,
            selectedHandoffs = emptyList(),
            status = BriefingStatus.NO_MAJOR_CHANGES
        )

        assertTrue(cycle.selectedHandoffs.isEmpty())
        assertEquals(BriefingStatus.NO_MAJOR_CHANGES, cycle.status)
    }

    private fun createMockHandoff(id: String) = IntelligenceCommunicationHandoff(
        packageId = id,
        intelligenceId = id,
        threadId = "t1",
        communicationPackage = IntelligenceCommunicationPackage(
            intelligenceId = id,
            threadId = "t1",
            changeId = null,
            currentState = mockk(relaxed = true),
            continuity = null,
            narrative = mockk(relaxed = true),
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            priority = AttentionLevel.IMPORTANT,
            evidenceSynthesis = mockk(relaxed = true),
            confidence = SignalConfidence.HIGH,
            conflicts = emptyList(),
            evidenceGap = mockk(relaxed = true),
            relevance = null,
            freshness = null,
            communicationReadiness = CommunicationReadinessResult(CommunicationReadinessLevel.READY, "R", CommunicationSafetyReason.SATEISFIED, false, Instant.now()),
            provenance = mockk(relaxed = true),
            assembledAt = Instant.now()
        ),
        authorizedAt = Instant.now()
    )
}
