package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceBriefingSelectionEngineTest {

    private val engine = DefaultIntelligenceBriefingSelectionEngine()

    @Test
    fun `high priority communication-ready intelligence is selected first`() {
        val s1 = createPrioritizedSignal("sig1", AttentionLevel.IMPORTANT)
        val p1 = createMockPackage("sig1", CommunicationReadinessLevel.READY, threadId = "t1")
        
        val s2 = createPrioritizedSignal("sig2", AttentionLevel.IMMEDIATE)
        val p2 = createMockPackage("sig2", CommunicationReadinessLevel.READY, threadId = "t2")

        val result = engine.selectBriefing(listOf(s1 to p1, s2 to p2))

        assertEquals(2, result.orderedSignalIds.size)
        // sig2 (IMMEDIATE) should be first
        assertEquals("sig2", result.orderedSignalIds[0])
    }

    @Test
    fun `NOT_READY intelligence is excluded`() {
        val s1 = createPrioritizedSignal("sig1", AttentionLevel.IMMEDIATE)
        val p1 = createMockPackage("sig1", CommunicationReadinessLevel.NOT_READY)

        val result = engine.selectBriefing(listOf(s1 to p1))

        assertTrue(result.orderedSignalIds.isEmpty())
        assertEquals(SelectionExclusionReason.NOT_COMMUNICATION_READY, result.excludedSignals["sig1"])
    }

    @Test
    fun `insignificant intelligence is excluded`() {
        val s1 = createPrioritizedSignal("sig1", AttentionLevel.IMMEDIATE, significance = SignificanceOutcome.NOT_SIGNIFICANT)
        val p1 = createMockPackage("sig1", CommunicationReadinessLevel.READY)

        val result = engine.selectBriefing(listOf(s1 to p1))

        assertTrue(result.orderedSignalIds.isEmpty())
        assertEquals(SelectionExclusionReason.NOT_SIGNIFICANT, result.excludedSignals["sig1"])
    }

    @Test
    fun `thread consolidation keeps the highest priority item`() {
        val s1 = createPrioritizedSignal("sig1", AttentionLevel.IMPORTANT)
        val p1 = createMockPackage("sig1", CommunicationReadinessLevel.READY, threadId = "t1")
        
        val s2 = createPrioritizedSignal("sig2", AttentionLevel.IMMEDIATE)
        val p2 = createMockPackage("sig2", CommunicationReadinessLevel.READY, threadId = "t1")

        val result = engine.selectBriefing(listOf(s1 to p1, s2 to p2))

        assertEquals(1, result.orderedSignalIds.size)
        assertEquals("sig2", result.orderedSignalIds[0])
        assertEquals(SelectionExclusionReason.DUPLICATE, result.excludedSignals["sig1"])
    }

    private fun createPrioritizedSignal(
        id: String,
        attention: AttentionLevel,
        significance: SignificanceOutcome = SignificanceOutcome.SIGNIFICANT
    ) = PrioritizedSignal(
        signal = Signal(
            id = id,
            title = "Title",
            summary = "Summary",
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.HIGH,
            significanceLevel = significance,
            detectedAt = Instant.now(),
            publishedAt = Instant.now(),
            source = SignalSource(name = "Source")
        ),
        attentionLevel = attention,
        rankingScore = 100
    )

    private fun createMockPackage(
        id: String, 
        readiness: CommunicationReadinessLevel,
        threadId: String = "t1"
    ) = IntelligenceCommunicationPackage(
        intelligenceId = id,
        threadId = threadId,
        changeId = null,
        currentState = mockk(relaxed = true),
        continuity = null,
        narrative = mockk(relaxed = true),
        significanceLevel = SignificanceOutcome.SIGNIFICANT,
        priority = AttentionLevel.IMMEDIATE,
        evidenceSynthesis = mockk(relaxed = true),
        confidence = SignalConfidence.HIGH,
        conflicts = emptyList(),
        evidenceGap = mockk(relaxed = true),
        relevance = null,
        freshness = null,
        communicationReadiness = CommunicationReadinessResult(
            level = readiness, 
            reason = "Reason", 
            requiresQualification = false, 
            evaluatedAt = Instant.now()
        ),
        provenance = mockk(relaxed = true),
        assembledAt = Instant.now()
    )
}
