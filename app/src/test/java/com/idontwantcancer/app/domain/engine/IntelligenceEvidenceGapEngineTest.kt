package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceEvidenceGapEngineTest {

    private val engine = DefaultIntelligenceEvidenceGapEngine()

    @Test
    fun `when unresolved conflict exists, gap is UNRESOLVED_CONFLICT`() {
        val thread = createMockThread("t1")
        val synthesis = createMockSynthesis(EvidenceSynthesisLevel.CONTESTED)
        val conflict = createMockConflict("c1", ResolutionStatus.UNRESOLVED)

        val result = engine.analyzeGaps(thread, synthesis, listOf(conflict))

        assertEquals(EvidenceGapLevel.UNRESOLVED_CONFLICT, result.level)
        assertEquals("c1", result.relatedConflictId)
    }

    @Test
    fun `when synthesis is STRONGLY_SUPPORTED, gap is NO_IDENTIFIED_GAP`() {
        val thread = createMockThread("t1")
        val synthesis = createMockSynthesis(EvidenceSynthesisLevel.STRONGLY_SUPPORTED, signalCount = 3)

        val result = engine.analyzeGaps(thread, synthesis, emptyList())

        assertEquals(EvidenceGapLevel.NO_IDENTIFIED_GAP, result.level)
    }

    @Test
    fun `when high-significance intelligence lacks corroboration, gap is MISSING_CORROBORATION`() {
        val thread = createMockThread("t1")
        // 'SUPPORTED' with only 1 signal implies single-source in our engine implementation
        val synthesis = createMockSynthesis(EvidenceSynthesisLevel.SUPPORTED, signalCount = 1)

        val result = engine.analyzeGaps(thread, synthesis, emptyList())

        assertEquals(EvidenceGapLevel.MISSING_CORROBORATION, result.level)
    }

    @Test
    fun `when evidence is limited, gap is INSUFFICIENT_SUPPORT`() {
        val thread = createMockThread("t1")
        val synthesis = createMockSynthesis(EvidenceSynthesisLevel.LIMITED)

        val result = engine.analyzeGaps(thread, synthesis, emptyList())

        assertEquals(EvidenceGapLevel.INSUFFICIENT_SUPPORT, result.level)
    }

    private fun createMockThread(id: String) = IntelligenceThread(
        id = id,
        topicIdentifier = "Topic",
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now()
    )

    private fun createMockSynthesis(level: EvidenceSynthesisLevel, signalCount: Int = 1) = EvidenceSynthesisResult(
        threadId = "t1",
        level = level,
        contributingSignalIds = List(signalCount) { "sig-$it" },
        conflictingSignalIds = emptyList(),
        reason = "Reason",
        synthesizedAt = Instant.now()
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
