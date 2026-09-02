package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class EvidenceConfidenceGateTest {

    private val gate = DefaultEvidenceConfidenceGate()

    @Test
    fun `when evidence is VERY_HIGH and no conflicts, confidence is VERY_HIGH`() {
        val assessment = createMockAssessment(strength = EvidenceStrength.VERY_HIGH)
        val source = createMockSource(trustLevel = SourceTrustLevel.VERY_HIGH)

        val decision = gate.evaluate(assessment, source, emptyList())

        assertEquals(SignalConfidence.VERY_HIGH, decision.confidence)
        assertTrue(decision.factors.any { it.name == "High-Authority Source" })
    }

    @Test
    fun `when unresolved conflicts exist, confidence is downgraded`() {
        val assessment = createMockAssessment(strength = EvidenceStrength.HIGH)
        val source = createMockSource(trustLevel = SourceTrustLevel.HIGH)
        val conflicts = listOf(
            IntelligenceConflict(
                id = "c1",
                topicIdentifier = "Topic",
                participatingSourceIds = listOf("S1", "S2"),
                type = ConflictType.DIRECT_CONTRADICTION,
                resolutionStatus = ResolutionStatus.UNRESOLVED,
                competingSignalIds = emptyList(),
                detectedAt = Instant.now()
            )
        )

        val decision = gate.evaluate(assessment, source, conflicts)

        // HIGH -> MODERATE due to conflict
        assertEquals(SignalConfidence.MODERATE, decision.confidence)
        assertTrue(decision.hasUnresolvedConflicts)
    }

    @Test
    fun `when source has limited reliability, confidence is downgraded`() {
        val assessment = createMockAssessment(strength = EvidenceStrength.MODERATE)
        val source = createMockSource(trustLevel = SourceTrustLevel.LOW)

        val decision = gate.evaluate(assessment, source, emptyList())

        // MODERATE -> LOW
        assertEquals(SignalConfidence.LOW, decision.confidence)
    }

    private fun createMockAssessment(strength: EvidenceStrength) = EvidenceAssessment(
        change = mockkChange(),
        strength = strength,
        factors = listOf(EvidenceFactor("High-Authority Source", "Desc", FactorImpact.POSITIVE)),
        assessedAt = Instant.now()
    )

    private fun mockkChange() = DetectedChange(
        id = "c1",
        type = ChangeType.MATERIAL_CHANGE,
        sourceId = "s1",
        contentId = "m1",
        description = "Desc",
        detectedAt = Instant.now()
    )

    private fun createMockSource(trustLevel: SourceTrustLevel) = IntelligenceSource(
        id = "s1",
        name = "Source",
        type = IntelligenceSourceType.PUBLIC_HEALTH,
        authority = SourceAuthority.INTERNATIONAL,
        reliability = SourceReliability(
            trustLevel = trustLevel,
            isTransparent = true,
            updateReliability = SourceTrustLevel.MODERATE
        )
    )
}
