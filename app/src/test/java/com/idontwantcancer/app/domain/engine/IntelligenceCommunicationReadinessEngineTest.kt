package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationReadinessEngineTest {

    private val engine = DefaultIntelligenceCommunicationReadinessEngine()

    @Test
    fun `well-supported significant intelligence is READY`() {
        val signal = createMockSignal(confidence = SignalConfidence.HIGH)
        val synthesis = createMockSynthesis(EvidenceSynthesisLevel.SUPPORTED)
        val gap = createMockGap(EvidenceGapLevel.NO_IDENTIFIED_GAP)
        
        val result = engine.evaluateReadiness(signal, synthesis, gap, emptyList())

        assertEquals(CommunicationReadinessLevel.READY, result.level)
        assertFalse(result.requiresQualification)
    }

    @Test
    fun `significant but conflicted intelligence is CONFLICTED`() {
        val signal = createMockSignal()
        val synthesis = createMockSynthesis()
        val gap = createMockGap()
        val conflict = createMockConflict(ResolutionStatus.UNRESOLVED)

        val result = engine.evaluateReadiness(signal, synthesis, gap, listOf(conflict))

        assertEquals(CommunicationReadinessLevel.CONFLICTED, result.level)
        assertTrue(result.requiresQualification)
    }

    @Test
    fun `intelligence with critical evidence gaps is INCOMPLETE`() {
        val signal = createMockSignal()
        val synthesis = createMockSynthesis()
        val gap = createMockGap(EvidenceGapLevel.INSUFFICIENT_SUPPORT)

        val result = engine.evaluateReadiness(signal, synthesis, gap, emptyList())

        assertEquals(CommunicationReadinessLevel.INCOMPLETE, result.level)
        assertTrue(result.requiresQualification)
    }

    @Test
    fun `intelligence with low confidence is READY_WITH_UNCERTAINTY`() {
        val signal = createMockSignal(confidence = SignalConfidence.LOW)
        val synthesis = createMockSynthesis(EvidenceSynthesisLevel.LIMITED)
        val gap = createMockGap(EvidenceGapLevel.NO_IDENTIFIED_GAP)

        val result = engine.evaluateReadiness(signal, synthesis, gap, emptyList())

        assertEquals(CommunicationReadinessLevel.READY_WITH_UNCERTAINTY, result.level)
        assertTrue(result.requiresQualification)
    }

    private fun createMockSignal(
        confidence: SignalConfidence = SignalConfidence.MODERATE
    ) = Signal(
        id = "sig1",
        title = "Title",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = confidence,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source")
    )

    private fun createMockSynthesis(
        level: EvidenceSynthesisLevel = EvidenceSynthesisLevel.SUPPORTED
    ) = EvidenceSynthesisResult(
        threadId = "t1",
        level = level,
        contributingSignalIds = emptyList(),
        conflictingSignalIds = emptyList(),
        reason = "Reason",
        synthesizedAt = Instant.now()
    )

    private fun createMockGap(
        level: EvidenceGapLevel = EvidenceGapLevel.NO_IDENTIFIED_GAP
    ) = EvidenceGapResult(
        threadId = "t1",
        level = level,
        reason = "Reason",
        analyzedAt = Instant.now()
    )

    private fun createMockConflict(status: ResolutionStatus) = IntelligenceConflict(
        id = "c1",
        topicIdentifier = "Topic",
        participatingSourceIds = listOf("S1", "S2"),
        type = ConflictType.DIRECT_CONTRADICTION,
        resolutionStatus = status,
        competingSignalIds = listOf("sig1", "sig2"),
        detectedAt = Instant.now()
    )
}
