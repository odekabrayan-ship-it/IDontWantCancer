package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationSafetyGateTest {

    private val gate = DefaultIntelligenceCommunicationSafetyGate()

    @Test
    fun `complete valid intelligence is READY`() {
        val pkg = createMockPackage()
        
        val result = gate.evaluateSafety(pkg)

        assertEquals(CommunicationReadinessLevel.READY, result.level)
        assertEquals(CommunicationSafetyReason.SATEISFIED, result.reasonCategory)
    }

    @Test
    fun `missing provenance blocks communication`() {
        val pkg = createMockPackage(sources = emptyList())

        val result = gate.evaluateSafety(pkg)

        assertEquals(CommunicationReadinessLevel.BLOCKED, result.level)
        assertEquals(CommunicationSafetyReason.MISSING_PROVENANCE, result.reasonCategory)
    }

    @Test
    fun `missing current state blocks communication`() {
        val pkg = createMockPackage(stateSummary = "")

        val result = gate.evaluateSafety(pkg)

        assertEquals(CommunicationReadinessLevel.BLOCKED, result.level)
        assertEquals(CommunicationSafetyReason.MISSING_CURRENT_STATE, result.reasonCategory)
    }

    @Test
    fun `insignificant intelligence is blocked if not defined`() {
        val pkg = createMockPackage(significance = null)

        val result = gate.evaluateSafety(pkg)

        assertEquals(CommunicationReadinessLevel.BLOCKED, result.level)
        assertEquals(CommunicationSafetyReason.INVALID_COMMUNICATION_PACKAGE, result.reasonCategory)
    }

    @Test
    fun `unresolved conflict does not automatically block communication`() {
        val pkg = createMockPackage(readinessLevel = CommunicationReadinessLevel.CONFLICTED)

        val result = gate.evaluateSafety(pkg)

        assertEquals(CommunicationReadinessLevel.CONFLICTED, result.level)
    }

    private fun createMockPackage(
        sources: List<SourceMaterial> = listOf(mockk(relaxed = true)),
        stateSummary: String = "Authoritative state.",
        significance: SignificanceOutcome? = SignificanceOutcome.SIGNIFICANT,
        readinessLevel: CommunicationReadinessLevel = CommunicationReadinessLevel.READY
    ) = IntelligenceCommunicationPackage(
        intelligenceId = "sig1",
        threadId = "t1",
        changeId = null,
        currentState = ReconstructedState("t1", stateSummary, "e1", reconstructedAt = Instant.now()),
        continuity = null,
        narrative = mockk(relaxed = true),
        significanceLevel = significance,
        priority = AttentionLevel.IMPORTANT,
        evidenceSynthesis = EvidenceSynthesisResult("t1", EvidenceSynthesisLevel.SUPPORTED, emptyList(), emptyList(), "Reason", Instant.now()),
        confidence = SignalConfidence.HIGH,
        conflicts = emptyList(),
        evidenceGap = EvidenceGapResult("t1", EvidenceGapLevel.NO_IDENTIFIED_GAP, "Reason", analyzedAt = Instant.now()),
        relevance = null,
        freshness = null,
        communicationReadiness = CommunicationReadinessResult(
            level = readinessLevel, 
            reason = "Reason", 
            requiresQualification = false, 
            evaluatedAt = Instant.now()
        ),
        provenance = IntelligenceProvenance(
            signalId = "sig1",
            signal = mockk(relaxed = true),
            thread = mockk(relaxed = true),
            timelineEntries = emptyList(),
            sources = sources,
            consolidatedEvent = null,
            reconstructedAt = Instant.now()
        ),
        assembledAt = Instant.now()
    )
}
