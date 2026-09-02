package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceContradictionResolutionEngineTest {

    private val engine = DefaultIntelligenceContradictionResolutionEngine()

    @Test
    fun `when titles are identical from different sources and no relation exists, detect direct contradiction`() = runTest {
        val existingSignal = createMockSignal(id = "sig1", title = "Risk A", source = "Source A")
        val newAssessment = createMockAssessment(title = "Risk A", sourceId = "Source B")

        val conflicts = engine.analyzeContradictions(newAssessment, listOf(existingSignal), emptyList())

        assertEquals(1, conflicts.size)
        assertEquals(ConflictType.DIRECT_CONTRADICTION, conflicts[0].type)
        assertEquals(ResolutionStatus.UNRESOLVED, conflicts[0].resolutionStatus)
    }

    @Test
    fun `when explicit supersession exists, resolution is SUPERSEDED`() = runTest {
        val existingSignal = createMockSignal(id = "sig1", title = "Risk A", source = "Source A")
        val newAssessment = createMockAssessment(id = "c1", title = "Risk A", sourceId = "Source B")
        val relation = SupersessionRelation("r1", "sig1", "c1", SupersessionType.REPLACEMENT, "Update", Instant.now())

        val conflicts = engine.analyzeContradictions(newAssessment, listOf(existingSignal), listOf(relation))

        assertEquals(1, conflicts.size)
        assertEquals(ConflictType.SUPERSESSION, conflicts[0].type)
        assertEquals(ResolutionStatus.SUPERSEDED, conflicts[0].resolutionStatus)
    }

    @Test
    fun `when explicit correction exists, resolution is SUPERSEDED and type is CORRECTION`() = runTest {
        val existingSignal = createMockSignal(id = "sig1", title = "Risk A", source = "Source A")
        val newAssessment = createMockAssessment(id = "c1", title = "Risk A", sourceId = "Source B")
        val relation = SupersessionRelation("r1", "sig1", "c1", SupersessionType.CORRECTION, "Correction", Instant.now())

        val conflicts = engine.analyzeContradictions(newAssessment, listOf(existingSignal), listOf(relation))

        assertEquals(1, conflicts.size)
        assertEquals(ConflictType.CORRECTION, conflicts[0].type)
        assertEquals(ResolutionStatus.SUPERSEDED, conflicts[0].resolutionStatus)
    }

    @Test
    fun `different topics are not contradictions`() = runTest {
        val existingSignal = createMockSignal(id = "sig1", title = "Risk A", source = "Source A")
        val newAssessment = createMockAssessment(title = "Risk B", sourceId = "Source B")

        val conflicts = engine.analyzeContradictions(newAssessment, listOf(existingSignal), emptyList())

        assertTrue(conflicts.isEmpty())
    }

    @Test
    fun `same source reports are ignored to avoid self-contradiction`() = runTest {
        val existingSignal = createMockSignal(id = "sig1", title = "Risk A", source = "Source A")
        val newAssessment = createMockAssessment(title = "Risk A", sourceId = "Source A")

        val conflicts = engine.analyzeContradictions(newAssessment, listOf(existingSignal), emptyList())

        assertTrue(conflicts.isEmpty())
    }

    private fun createMockSignal(
        id: String, 
        title: String, 
        source: String,
        category: SignalCategory = SignalCategory.RESEARCH
    ) = Signal(
        id = id,
        title = title,
        summary = "Summary",
        category = category,
        importance = SignalImportance.MODERATE,
        confidence = SignalConfidence.MODERATE,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = source)
    )

    private fun createMockAssessment(id: String = "c1", title: String, sourceId: String) = EvidenceAssessment(
        change = DetectedChange(
            id = id,
            type = ChangeType.NEW_INFORMATION,
            sourceId = sourceId,
            contentId = "m1",
            description = title,
            detectedAt = Instant.now()
        ),
        strength = EvidenceStrength.HIGH,
        factors = emptyList(),
        assessedAt = Instant.now()
    )
}
