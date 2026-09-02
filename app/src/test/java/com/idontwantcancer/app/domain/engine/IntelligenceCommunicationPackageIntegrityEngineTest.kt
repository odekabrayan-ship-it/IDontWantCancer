package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationPackageIntegrityEngineTest {

    private val engine = DefaultIntelligenceCommunicationPackageIntegrityEngine()

    @Test
    fun `valid package returns VALID status`() {
        val pkg = createMockPackage()
        
        val result = engine.verifyPackageIntegrity(pkg)

        assertEquals(CommunicationIntegrityStatus.VALID, result.status)
        assertEquals(0, result.violations.size)
    }

    @Test
    fun `identity mismatch returns INVALID status`() {
        val pkg = createMockPackage(intelligenceId = "mismatch-id")

        val result = engine.verifyPackageIntegrity(pkg)

        assertEquals(CommunicationIntegrityStatus.INVALID, result.status)
        assertEquals(CommunicationIntegrityViolationType.IDENTITY_MISMATCH, result.violations[0].type)
    }

    @Test
    fun `thread mismatch returns INVALID status`() {
        val pkg = createMockPackage(threadId = "wrong-thread")

        val result = engine.verifyPackageIntegrity(pkg)

        assertEquals(CommunicationIntegrityStatus.INVALID, result.status)
        assertEquals(CommunicationIntegrityViolationType.THREAD_MISMATCH, result.violations[0].type)
    }

    @Test
    fun `evidence confidence contradiction returns INVALID status`() {
        val pkg = createMockPackage(confidence = SignalConfidence.LOW) // Provenance signal has HIGH

        val result = engine.verifyPackageIntegrity(pkg)

        assertEquals(CommunicationIntegrityStatus.INVALID, result.status)
        assertEquals(CommunicationIntegrityViolationType.EVIDENCE_MISMATCH, result.violations[0].type)
    }

    private fun createMockPackage(
        intelligenceId: String = "sig1",
        threadId: String = "t1",
        confidence: SignalConfidence = SignalConfidence.HIGH
    ) = IntelligenceCommunicationPackage(
        intelligenceId = intelligenceId,
        threadId = threadId,
        changeId = null,
        currentState = ReconstructedState("t1", "Summary", "e1", reconstructedAt = Instant.now()),
        continuity = null,
        narrative = mockk(relaxed = true),
        significanceLevel = SignificanceOutcome.SIGNIFICANT,
        priority = AttentionLevel.IMPORTANT,
        evidenceSynthesis = EvidenceSynthesisResult("t1", EvidenceSynthesisLevel.SUPPORTED, emptyList(), emptyList(), "Reason", Instant.now()),
        confidence = confidence,
        conflicts = emptyList(),
        evidenceGap = EvidenceGapResult("t1", EvidenceGapLevel.NO_IDENTIFIED_GAP, "Reason", analyzedAt = Instant.now()),
        relevance = null,
        freshness = null,
        communicationReadiness = CommunicationReadinessResult(
            level = CommunicationReadinessLevel.READY, 
            reason = "Reason", 
            requiresQualification = false, 
            evaluatedAt = Instant.now()
        ),
        provenance = IntelligenceProvenance(
            signalId = "sig1",
            signal = Signal(
                id = "sig1",
                title = "Title",
                summary = "Summary",
                category = SignalCategory.RESEARCH,
                importance = SignalImportance.HIGH,
                confidence = SignalConfidence.HIGH,
                significanceLevel = SignificanceOutcome.SIGNIFICANT,
                detectedAt = Instant.now(),
                publishedAt = Instant.now(),
                source = SignalSource(name = "Source")
            ),
            thread = mockk(relaxed = true),
            timelineEntries = emptyList(),
            sources = listOf(mockk(relaxed = true)),
            consolidatedEvent = null,
            reconstructedAt = Instant.now()
        ),
        assembledAt = Instant.now()
    )
}
