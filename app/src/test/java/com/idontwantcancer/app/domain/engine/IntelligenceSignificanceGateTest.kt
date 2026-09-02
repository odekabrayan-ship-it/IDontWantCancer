package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceSignificanceGateTest {

    private val gate = DefaultIntelligenceSignificanceGate()

    @Test
    fun `when change type is NO_MEANINGFUL_CHANGE, outcome is NOT_SIGNIFICANT`() {
        val change = createMockChange(type = ChangeType.NO_MEANINGFUL_CHANGE)
        val assessment = createMockAssessment(strength = EvidenceStrength.HIGH)
        val source = createMockSource()

        val decision = gate.evaluate(change, assessment, source)

        assertEquals(SignificanceOutcome.NOT_SIGNIFICANT, decision.outcome)
    }

    @Test
    fun `when change type is SAFETY_ACTION, outcome is HIGH_SIGNIFICANCE`() {
        val change = createMockChange(type = ChangeType.SAFETY_ACTION)
        val assessment = createMockAssessment(strength = EvidenceStrength.MODERATE)
        val source = createMockSource()

        val decision = gate.evaluate(change, assessment, source)

        assertEquals(SignificanceOutcome.HIGH_SIGNIFICANCE, decision.outcome)
    }

    @Test
    fun `when evidence is strong from international source, outcome is CRITICAL`() {
        val change = createMockChange(type = ChangeType.MATERIAL_CHANGE)
        val assessment = createMockAssessment(strength = EvidenceStrength.HIGH)
        val source = createMockSource(authority = SourceAuthority.INTERNATIONAL)

        val decision = gate.evaluate(change, assessment, source)

        assertEquals(SignificanceOutcome.CRITICAL, decision.outcome)
    }

    @Test
    fun `when evidence is LOW and change is not critical, outcome is NOT_SIGNIFICANT`() {
        val change = createMockChange(type = ChangeType.MATERIAL_CHANGE)
        val assessment = createMockAssessment(strength = EvidenceStrength.LOW)
        val source = createMockSource()

        val decision = gate.evaluate(change, assessment, source)

        assertEquals(SignificanceOutcome.NOT_SIGNIFICANT, decision.outcome)
    }

    private fun createMockChange(type: ChangeType) = DetectedChange(
        id = "c1",
        type = type,
        sourceId = "s1",
        contentId = "m1",
        description = "Test Change",
        detectedAt = Instant.now()
    )

    private fun createMockAssessment(strength: EvidenceStrength) = EvidenceAssessment(
        change = createMockChange(ChangeType.MATERIAL_CHANGE),
        strength = strength,
        factors = emptyList(),
        assessedAt = Instant.now()
    )

    private fun createMockSource(authority: SourceAuthority = SourceAuthority.NATIONAL) = IntelligenceSource(
        id = "s1",
        name = "Source",
        type = IntelligenceSourceType.PUBLIC_HEALTH,
        authority = authority,
        reliability = SourceReliability(
            trustLevel = SourceTrustLevel.MODERATE,
            isTransparent = true,
            updateReliability = SourceTrustLevel.MODERATE
        )
    )
}
