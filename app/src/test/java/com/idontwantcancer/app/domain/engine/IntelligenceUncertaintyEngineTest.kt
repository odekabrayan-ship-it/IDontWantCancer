package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceUncertaintyEngineTest {

    private val engine = DefaultIntelligenceUncertaintyEngine()

    @Test
    fun `strong supported evidence results in low uncertainty flags`() {
        val pkg = createMockPackage(
            synthesis = EvidenceSynthesisLevel.SUPPORTED,
            confidence = SignalConfidence.HIGH,
            gap = EvidenceGapLevel.NO_IDENTIFIED_GAP,
            resolution = ResolutionStatus.RESOLVED
        )

        val model = engine.determineUncertainty(pkg)

        assertFalse(model.isContested)
        assertFalse(model.isDeveloping)
        assertFalse(model.isIncomplete)
        assertFalse(model.requiresQualification)
    }

    @Test
    fun `unresolved conflict results in isContested flag`() {
        val pkg = createMockPackage(
            resolution = ResolutionStatus.UNRESOLVED
        )

        val model = engine.determineUncertainty(pkg)

        assertTrue(model.isContested)
        assertTrue(model.requiresQualification)
    }

    @Test
    fun `low confidence results in isDeveloping flag`() {
        val pkg = createMockPackage(
            confidence = SignalConfidence.LOW
        )

        val model = engine.determineUncertainty(pkg)

        assertTrue(model.isDeveloping)
        assertTrue(model.requiresQualification)
    }

    @Test
    fun `evidence gaps result in isIncomplete flag`() {
        val pkg = createMockPackage(
            gap = EvidenceGapLevel.INSUFFICIENT_SUPPORT
        )

        val model = engine.determineUncertainty(pkg)

        assertTrue(model.isIncomplete)
        assertTrue(model.requiresQualification)
    }

    private fun createMockPackage(
        synthesis: EvidenceSynthesisLevel = EvidenceSynthesisLevel.SUPPORTED,
        confidence: SignalConfidence = SignalConfidence.HIGH,
        gap: EvidenceGapLevel = EvidenceGapLevel.NO_IDENTIFIED_GAP,
        resolution: ResolutionStatus = ResolutionStatus.RESOLVED
    ) = IntelligenceCommunicationPackage(
        intelligenceId = "sig1",
        threadId = "t1",
        changeId = null,
        currentState = mockk(relaxed = true),
        continuity = null,
        narrative = mockk(relaxed = true),
        significanceLevel = SignificanceOutcome.SIGNIFICANT,
        priority = AttentionLevel.IMPORTANT,
        evidenceSynthesis = EvidenceSynthesisResult("t1", synthesis, emptyList(), emptyList(), "Reason", Instant.now()),
        confidence = confidence,
        conflicts = if (resolution == ResolutionStatus.UNRESOLVED) listOf(createMockConflict()) else emptyList(),
        evidenceGap = EvidenceGapResult("t1", gap, "Reason", analyzedAt = Instant.now()),
        relevance = null,
        freshness = null,
        communicationReadiness = CommunicationReadinessResult(
            level = CommunicationReadinessLevel.READY, 
            reason = "Reason", 
            requiresQualification = false, 
            evaluatedAt = Instant.now()
        ),
        provenance = mockk(relaxed = true),
        assembledAt = Instant.now()
    )

    private fun createMockConflict() = IntelligenceConflict(
        id = "c1",
        topicIdentifier = "Topic",
        participatingSourceIds = listOf("S1", "S2"),
        type = ConflictType.DIRECT_CONTRADICTION,
        resolutionStatus = ResolutionStatus.UNRESOLVED,
        competingSignalIds = listOf("sig1", "sig2"),
        detectedAt = Instant.now()
    )
}
