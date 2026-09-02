package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class EvidenceAssessmentEngineTest {

    private val engine = DefaultEvidenceAssessmentEngine()

    @Test
    fun `when primary source is international, base strength is HIGH`() = runTest {
        val change = createMockChange()
        val source = createMockSource(authority = SourceAuthority.INTERNATIONAL)

        val assessment = engine.assess(change, source)

        // INTERNATIONAL = HIGH. DEFAULT trust = MODERATE. Result = HIGH.
        assertEquals(EvidenceStrength.HIGH, assessment.strength)
        assertTrue(assessment.factors.any { it.name == "Source Authority" })
    }

    @Test
    fun `when primary source is academic, base strength is LOW`() = runTest {
        val change = createMockChange()
        val source = createMockSource(authority = SourceAuthority.INDEPENDENT_ACADEMIC)

        val assessment = engine.assess(change, source)

        assertEquals(EvidenceStrength.LOW, assessment.strength)
        assertTrue(assessment.hasUncertainty)
    }

    @Test
    fun `when source has exceptional trust, strength is upgraded`() = runTest {
        val change = createMockChange()
        val source = createMockSource(
            authority = SourceAuthority.NATIONAL, // MODERATE
            trustLevel = SourceTrustLevel.VERY_HIGH // Upgrade
        )

        val assessment = engine.assess(change, source)

        assertEquals(EvidenceStrength.HIGH, assessment.strength)
    }

    @Test
    fun `when corroborated by multiple sources, strength is upgraded`() = runTest {
        val change = createMockChange()
        val source = createMockSource(authority = SourceAuthority.NATIONAL) // MODERATE
        val corroborators = listOf(
            createMockSource(id = "c1"),
            createMockSource(id = "c2")
        )

        val assessment = engine.assess(change, source, corroborators)

        // MODERATE (Base) + 1 level (Corroboration) = HIGH
        assertEquals(EvidenceStrength.HIGH, assessment.strength)
    }

    private fun createMockChange() = DetectedChange(
        id = "c1",
        type = ChangeType.NEW_INFORMATION,
        sourceId = "test-source",
        contentId = "test-id",
        description = "Test Change",
        detectedAt = Instant.now(),
        previousStateReference = null,
        currentStateReference = "new-hash"
    )

    private fun createMockSource(
        id: String = "test-source",
        authority: SourceAuthority = SourceAuthority.NATIONAL,
        trustLevel: SourceTrustLevel = SourceTrustLevel.MODERATE
    ) = IntelligenceSource(
        id = id,
        name = "Test Source",
        type = IntelligenceSourceType.PUBLIC_HEALTH,
        authority = authority,
        reliability = SourceReliability(
            trustLevel = trustLevel,
            isTransparent = true,
            updateReliability = SourceTrustLevel.MODERATE
        )
    )
}
