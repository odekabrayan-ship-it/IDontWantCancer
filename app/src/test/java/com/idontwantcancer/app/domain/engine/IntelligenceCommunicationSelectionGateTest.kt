package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationSelectionGateTest {

    private val gate = DefaultIntelligenceCommunicationSelectionGate()

    @Test
    fun `valid ordered items remain selected`() {
        val h1 = createMockHandoff("sig1", threadId = "t1")
        val h2 = createMockHandoff("sig2", threadId = "t2")

        val result = gate.selectEligible(listOf(h1, h2))

        assertEquals(2, result.selectedHandoffs.size)
        assertEquals("sig1", result.selectedHandoffs[0].intelligenceId)
        assertEquals("sig2", result.selectedHandoffs[1].intelligenceId)
    }

    @Test
    fun `communication-blocked items are excluded`() {
        val h1 = createMockHandoff("sig1", readiness = CommunicationReadinessLevel.BLOCKED)

        val result = gate.selectEligible(listOf(h1))

        assertTrue(result.selectedHandoffs.isEmpty())
        assertTrue(result.excludedHandoffs.containsKey("sig1"))
    }

    @Test
    fun `thread uniqueness per cycle is respected`() {
        // Two handoffs for the same thread
        val h1 = createMockHandoff("sig1", threadId = "t1")
        val h2 = createMockHandoff("sig2", threadId = "t1")

        val result = gate.selectEligible(listOf(h1, h2))

        assertEquals(1, result.selectedHandoffs.size)
        assertEquals("sig1", result.selectedHandoffs[0].intelligenceId)
        assertTrue(result.excludedHandoffs.containsKey("sig2"))
    }

    @Test
    fun `selection preserves incoming order`() {
        val h1 = createMockHandoff("sig1", threadId = "t1")
        val h2 = createMockHandoff("sig2", threadId = "t2")
        val h3 = createMockHandoff("sig3", threadId = "t3")

        val result = gate.selectEligible(listOf(h1, h2, h3))

        assertEquals(3, result.selectedHandoffs.size)
        assertEquals("sig1", result.selectedHandoffs[0].intelligenceId)
        assertEquals("sig2", result.selectedHandoffs[1].intelligenceId)
        assertEquals("sig3", result.selectedHandoffs[2].intelligenceId)
    }

    private fun createMockHandoff(
        id: String, 
        threadId: String = "t1",
        readiness: CommunicationReadinessLevel = CommunicationReadinessLevel.READY
    ) = IntelligenceCommunicationHandoff(
        packageId = id,
        intelligenceId = id,
        threadId = threadId,
        communicationPackage = IntelligenceCommunicationPackage(
            intelligenceId = id,
            threadId = threadId,
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
            communicationReadiness = CommunicationReadinessResult(readiness, "R", CommunicationSafetyReason.SATEISFIED, false, Instant.now()),
            provenance = mockk(relaxed = true),
            assembledAt = Instant.now()
        ),
        authorizedAt = Instant.now()
    )
}
