package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceBriefingContextEngineTest {

    private val engine = DefaultIntelligenceBriefingContextEngine()

    @Test
    fun `reversal requires prior state context`() {
        val pkg = createMockPackage(continuity = ContinuityLevel.REVERSAL, prevEntryId = "old-state")

        val context = engine.determineContext(pkg)

        assertEquals("old-state", context.previousStateEntryId)
        assertTrue(context.reason.contains("Prior state context required"))
    }

    @Test
    fun `routine continuation does not require prior state context`() {
        val pkg = createMockPackage(continuity = ContinuityLevel.CONTINUATION)

        val context = engine.determineContext(pkg)

        assertNull(context.previousStateEntryId)
    }

    @Test
    fun `unresolved conflict is included in context`() {
        val conflict = createMockConflict("c1", ResolutionStatus.UNRESOLVED)
        val pkg = createMockPackage(conflicts = listOf(conflict))

        val context = engine.determineContext(pkg)

        assertEquals(1, context.relevantConflictIds.size)
        assertEquals("c1", context.relevantConflictIds[0])
    }

    private fun createMockPackage(
        continuity: ContinuityLevel? = null,
        prevEntryId: String? = null,
        conflicts: List<IntelligenceConflict> = emptyList()
    ) = IntelligenceCommunicationPackage(
        intelligenceId = "sig1",
        threadId = "t1",
        changeId = null,
        currentState = ReconstructedState("t1", "Summ", "e1", reconstructedAt = Instant.now()),
        continuity = continuity?.let { IntelligenceContinuityResult(it, "t1", "sig1", prevEntryId, "Reason", Instant.now()) },
        narrative = IntelligenceNarrativeSequence("t1", "Topic", emptyList()),
        significanceLevel = SignificanceOutcome.SIGNIFICANT,
        priority = AttentionLevel.IMPORTANT,
        evidenceSynthesis = mockk(relaxed = true),
        confidence = SignalConfidence.HIGH,
        conflicts = conflicts,
        evidenceGap = EvidenceGapResult("t1", EvidenceGapLevel.NO_IDENTIFIED_GAP, "Reason", analyzedAt = Instant.now()),
        relevance = null,
        freshness = null,
        communicationReadiness = mockk(relaxed = true),
        provenance = mockk(relaxed = true),
        assembledAt = Instant.now()
    )

    private fun createMockConflict(id: String, status: ResolutionStatus) = IntelligenceConflict(
        id = id,
        topicIdentifier = "Topic",
        participatingSourceIds = listOf("S1", "S2"),
        type = ConflictType.DIRECT_CONTRADICTION,
        resolutionStatus = status,
        competingSignalIds = listOf("sig1", "sig2"),
        detectedAt = Instant.now()
    )
}
