package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceBriefingChangeAwarenessEngineTest {

    private val engine = DefaultIntelligenceBriefingChangeAwarenessEngine()

    @Test
    fun `identical briefings produce UNCHANGED results`() {
        val b1 = createMockBriefing("b1", listOf("sig1"))
        val b2 = createMockBriefing("b2", listOf("sig1"))

        val result = engine.detectBriefingChanges(b2, b1)

        assertFalse(result.hasMeaningfulChange)
        assertEquals(BriefingItemChangeLevel.UNCHANGED, result.itemChanges[0].level)
    }

    @Test
    fun `new briefing item is classified as NEW`() {
        val b1 = createMockBriefing("b1", emptyList<String>())
        val b2 = createMockBriefing("b2", listOf("sig1"))

        val result = engine.detectBriefingChanges(b2, b1)

        assertTrue(result.hasMeaningfulChange)
        assertEquals(BriefingItemChangeLevel.NEW, result.itemChanges[0].level)
    }

    @Test
    fun `material change is detected from continuity reference`() {
        val i1 = createBriefingItem("sig1", stateRef = "e1")
        val b1 = createMockBriefingWithItems("b1", listOf(i1))
        
        val i2 = createBriefingItem("sig1", stateRef = "e2", continuity = ContinuityLevel.MATERIAL_CHANGE)
        val b2 = createMockBriefingWithItems("b2", listOf(i2))

        val result = engine.detectBriefingChanges(b2, b1)

        assertEquals(BriefingItemChangeLevel.MATERIALLY_CHANGED, result.itemChanges[0].level)
    }

    @Test
    fun `removal from briefing is detected`() {
        val b1 = createMockBriefing("b1", listOf("sig1"))
        val b2 = createMockBriefing("b2", emptyList<String>())

        val result = engine.detectBriefingChanges(b2, b1)

        assertEquals(BriefingItemChangeLevel.REMOVED_FROM_BRIEFING, result.itemChanges[0].level)
    }

    private fun createMockBriefing(id: String, signalIds: List<String>): IntelligenceBriefing {
        return IntelligenceBriefing(
            id = id,
            cycleId = "c1",
            generatedAt = Instant.now(),
            status = BriefingStatus.READY,
            items = signalIds.mapIndexed { index, sid -> createBriefingItem(sid, position = index) },
            signals = emptyMap()
        )
    }

    private fun createMockBriefingWithItems(id: String, items: List<IntelligenceBriefingItem>): IntelligenceBriefing {
        return IntelligenceBriefing(
            id = id,
            cycleId = "c1",
            generatedAt = Instant.now(),
            status = BriefingStatus.READY,
            items = items,
            signals = emptyMap()
        )
    }

    private fun createBriefingItem(
        sid: String, 
        position: Int = 0, 
        stateRef: String = "e1",
        continuity: ContinuityLevel? = null
    ) = IntelligenceBriefingItem(
        id = "item-$sid",
        position = position,
        communicationPackageId = sid,
        intelligenceId = sid,
        threadId = "t1",
        currentStateReference = stateRef,
        changeReference = null,
        continuityReference = continuity,
        significanceReference = SignificanceOutcome.SIGNIFICANT,
        priorityReference = AttentionLevel.ROUTINE,
        evidenceReference = EvidenceSynthesisLevel.SUPPORTED,
        uncertaintyReference = SignalConfidence.MODERATE,
        conflictReference = false,
        narrativeReference = "t1",
        provenanceReference = sid,
        readiness = CommunicationReadinessLevel.READY,
        inclusionReason = "Reason"
    )
}
