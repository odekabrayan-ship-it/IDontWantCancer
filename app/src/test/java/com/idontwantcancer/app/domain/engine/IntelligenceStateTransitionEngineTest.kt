package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.Instant

class IntelligenceStateTransitionEngineTest {

    private val engine = DefaultIntelligenceStateTransitionEngine()

    @Test
    fun `initial state established from initial observation`() {
        val entry = createEntry(id = "1", type = TimelineEntryType.INITIAL_OBSERVATION)
        val synthesis = createMockSynthesis(level = EvidenceSynthesisLevel.SUPPORTED)
        
        val transition = engine.evaluateTransition(null, entry, emptyList(), emptyList(), synthesis)

        assertNotNull(transition)
        assertEquals(IntelligenceStateTransitionType.INITIALIZED, transition!!.type)
        assertEquals("1", transition.resultingStateEntryId)
    }

    @Test
    fun `valid supersession creates REPLACED transition`() {
        val previous = createMockState(entryId = "1")
        val entry = createEntry(id = "2", type = TimelineEntryType.REGULATORY_ACTION)
        val relation = SupersessionRelation("r1", "1", "2", SupersessionType.REPLACEMENT, "Reason", Instant.now())
        val synthesis = createMockSynthesis(level = EvidenceSynthesisLevel.SUPPORTED)

        val transition = engine.evaluateTransition(previous, entry, listOf(relation), emptyList(), synthesis)

        assertNotNull(transition)
        assertEquals(IntelligenceStateTransitionType.REPLACED, transition!!.type)
    }

    @Test
    fun `correction creates CORRECTED transition`() {
        val previous = createMockState(entryId = "1")
        val entry = createEntry(id = "2", type = TimelineEntryType.CORRECTION)
        val relation = SupersessionRelation("r1", "1", "2", SupersessionType.CORRECTION, "Reason", Instant.now())
        val synthesis = createMockSynthesis(level = EvidenceSynthesisLevel.SUPPORTED)

        val transition = engine.evaluateTransition(previous, entry, listOf(relation), emptyList(), synthesis)

        assertNotNull(transition)
        assertEquals(IntelligenceStateTransitionType.CORRECTED, transition!!.type)
    }

    @Test
    fun `new conflict creates CONTESTED transition via synthesis`() {
        val previous = createMockState(entryId = "1", conflict = ResolutionStatus.RESOLVED)
        val entry = createEntry(id = "2")
        val conflict = IntelligenceConflict("c1", "Topic", listOf("S1", "S2"), ConflictType.DIRECT_CONTRADICTION, ResolutionStatus.UNRESOLVED, emptyList(), Instant.now())
        val synthesis = createMockSynthesis(level = EvidenceSynthesisLevel.CONTESTED)

        val transition = engine.evaluateTransition(previous, entry, emptyList(), listOf(conflict), synthesis)

        assertNotNull(transition)
        assertEquals(IntelligenceStateTransitionType.CONTESTED, transition!!.type)
    }

    private fun createEntry(
        id: String,
        type: TimelineEntryType = TimelineEntryType.MATERIAL_CHANGE
    ) = TimelineEntry(
        id = id,
        threadId = "t1",
        type = type,
        description = "Desc",
        ingestionTime = Instant.now()
    )

    private fun createMockState(entryId: String, conflict: ResolutionStatus = ResolutionStatus.RESOLVED) = ReconstructedState(
        threadId = "t1",
        summary = "Summary",
        effectiveEntryId = entryId,
        confidence = SignalConfidence.HIGH,
        conflictStatus = conflict,
        reconstructedAt = Instant.now()
    )

    private fun createMockSynthesis(level: EvidenceSynthesisLevel) = EvidenceSynthesisResult(
        threadId = "t1",
        level = level,
        contributingSignalIds = emptyList(),
        conflictingSignalIds = emptyList(),
        reason = "Reason",
        synthesizedAt = Instant.now()
    )
}
