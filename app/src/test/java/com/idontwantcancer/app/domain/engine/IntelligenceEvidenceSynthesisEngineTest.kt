package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceEvidenceSynthesisEngineTest {

    private val engine = DefaultIntelligenceEvidenceSynthesisEngine()

    @Test
    fun `when single signal exists, synthesis level is SUPPORTED`() {
        val signals = listOf(createMockSignal("sig1", SignalConfidence.HIGH, "Source A"))
        
        val result = engine.synthesizeEvidence("t1", signals, emptyList())

        assertEquals(EvidenceSynthesisLevel.SUPPORTED, result.level)
    }

    @Test
    fun `independent corroboration from 3 sources results in STRONGLY_SUPPORTED`() {
        val s1 = createMockSignal("sig1", SignalConfidence.HIGH, "Source A")
        val s2 = createMockSignal("sig2", SignalConfidence.HIGH, "Source B")
        val s3 = createMockSignal("sig3", SignalConfidence.HIGH, "Source C")
        
        val result = engine.synthesizeEvidence("t1", listOf(s1, s2, s3), emptyList())

        assertEquals(EvidenceSynthesisLevel.STRONGLY_SUPPORTED, result.level)
    }

    @Test
    fun `unresolved conflict results in CONTESTED`() {
        val signals = listOf(createMockSignal("sig1", SignalConfidence.HIGH, "Source A"))
        val conflicts = listOf(
            IntelligenceConflict(
                id = "c1",
                topicIdentifier = "Topic",
                participatingSourceIds = listOf("Source A", "Source B"),
                type = ConflictType.DIRECT_CONTRADICTION,
                resolutionStatus = ResolutionStatus.UNRESOLVED,
                competingSignalIds = listOf("sig1", "sig2"),
                detectedAt = Instant.now()
            )
        )

        val result = engine.synthesizeEvidence("t1", signals, conflicts)

        assertEquals(EvidenceSynthesisLevel.CONTESTED, result.level)
    }

    @Test
    fun `duplicate reporting from same source does not establish strong support`() {
        // Source A reporting twice (e.g. repeated study or summary)
        val s1 = createMockSignal("sig1", SignalConfidence.HIGH, "Source A")
        val s2 = createMockSignal("sig2", SignalConfidence.HIGH, "Source A")
        
        val result = engine.synthesizeEvidence("t1", listOf(s1, s2), emptyList())

        // Still only 1 independent source
        assertEquals(EvidenceSynthesisLevel.SUPPORTED, result.level)
    }

    private fun createMockSignal(id: String, confidence: SignalConfidence, sourceName: String) = Signal(
        id = id,
        title = "Title",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = confidence,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = sourceName)
    )
}
