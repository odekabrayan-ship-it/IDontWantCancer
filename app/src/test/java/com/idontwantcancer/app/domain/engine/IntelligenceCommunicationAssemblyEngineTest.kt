package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationAssemblyEngineTest {

    private val engine = DefaultIntelligenceCommunicationAssemblyEngine()

    @Test
    fun `assemblePackage produces a complete structured package`() {
        val signal = createMockSignal()
        val thread = createMockThread()
        val currentState = createMockCurrentState()
        val synthesis = createMockSynthesis()
        val gap = createMockGap()
        val readiness = createMockReadiness()
        val provenance = createMockProvenance()
        val narrative = IntelligenceNarrativeSequence("t1", "Topic", emptyList())

        val pkg = engine.assemblePackage(
            signal = signal,
            thread = thread,
            currentState = currentState,
            continuity = null,
            narrative = narrative,
            synthesis = synthesis,
            gap = gap,
            readiness = readiness,
            provenance = provenance,
            conflicts = emptyList(),
            prioritized = null,
            relevance = null,
            freshness = null
        )

        assertEquals(signal.id, pkg.intelligenceId)
        assertEquals(thread.id, pkg.threadId)
        assertEquals(currentState, pkg.currentState)
        assertEquals(synthesis, pkg.evidenceSynthesis)
        assertEquals(gap, pkg.evidenceGap)
        assertEquals(readiness, pkg.communicationReadiness)
        assertEquals(provenance, pkg.provenance)
        assertNotNull(pkg.assembledAt)
    }

    private fun createMockSignal() = Signal(
        id = "sig1",
        title = "Title",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = SignalConfidence.HIGH,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source")
    )

    private fun createMockThread() = IntelligenceThread(
        id = "t1",
        topicIdentifier = "Topic",
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now()
    )

    private fun createMockCurrentState() = ReconstructedState(
        threadId = "t1",
        summary = "Summary",
        effectiveEntryId = "e1",
        reconstructedAt = Instant.now()
    )

    private fun createMockSynthesis() = EvidenceSynthesisResult(
        threadId = "t1",
        level = EvidenceSynthesisLevel.SUPPORTED,
        contributingSignalIds = emptyList(),
        conflictingSignalIds = emptyList(),
        reason = "Reason",
        synthesizedAt = Instant.now()
    )

    private fun createMockGap() = EvidenceGapResult(
        threadId = "t1",
        level = EvidenceGapLevel.NO_IDENTIFIED_GAP,
        reason = "Reason",
        analyzedAt = Instant.now()
    )

    private fun createMockReadiness() = CommunicationReadinessResult(
        level = CommunicationReadinessLevel.READY,
        reason = "Reason",
        requiresQualification = false,
        evaluatedAt = Instant.now()
    )

    private fun createMockProvenance() = IntelligenceProvenance(
        signalId = "sig1",
        signal = createMockSignal(),
        thread = createMockThread(),
        timelineEntries = emptyList(),
        sources = emptyList(),
        consolidatedEvent = null,
        reconstructedAt = Instant.now()
    )
}
