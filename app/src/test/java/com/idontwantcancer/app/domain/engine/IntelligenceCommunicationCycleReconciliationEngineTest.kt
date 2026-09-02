package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationCycleReconciliationEngineTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val stateReconstructor = mockk<IntelligenceStateReconstructionEngine>()
    private val synthesisEngine = mockk<IntelligenceEvidenceSynthesisEngine>()
    
    private val engine = DefaultIntelligenceCommunicationCycleReconciliationEngine(
        memory, stateReconstructor, synthesisEngine
    )

    @Test
    fun `identical historical and current intelligence returns UNCHANGED`() = runTest {
        val now = Instant.now()
        val briefing = createMockBriefing("b1", "sig1")
        val signal = briefing.signals["sig1"]!!
        val thread = IntelligenceThread("t1", "Topic", now, now)

        coEvery { memory.getThreadById("t1") } returns thread
        coEvery { memory.getTimelineEntriesForThread("t1") } returns emptyList()
        coEvery { memory.getSignalById("sig1") } returns signal
        coEvery { memory.getConflictsForTopic(any()) } returns emptyList()
        coEvery { stateReconstructor.reconstructCurrentState(any()) } returns ReconstructedState("t1", "Sum", "e1", reconstructedAt = now)
        every { synthesisEngine.synthesizeEvidence(any(), any(), any()) } returns EvidenceSynthesisResult("t1", EvidenceSynthesisLevel.SUPPORTED, emptyList(), emptyList(), "R", now)

        val result = engine.reconcileCycle(briefing)

        assertEquals(CommunicationReconciliationStatus.UNCHANGED, result.status)
        assertEquals(1, result.itemDiffs.size)
    }

    @Test
    fun `current state change is detected`() = runTest {
        val now = Instant.now()
        val briefing = createMockBriefing("b1", "sig1")
        val signal = briefing.signals["sig1"]!!
        
        coEvery { memory.getThreadById(any()) } returns mockk(relaxed = true)
        coEvery { memory.getTimelineEntriesForThread(any()) } returns emptyList()
        coEvery { memory.getSignalById(any()) } returns signal
        coEvery { memory.getConflictsForTopic(any()) } returns emptyList()
        // Force state change
        coEvery { stateReconstructor.reconstructCurrentState(any()) } returns ReconstructedState("t1", "Sum", "e2", reconstructedAt = now)
        every { synthesisEngine.synthesizeEvidence(any(), any(), any()) } returns EvidenceSynthesisResult("t1", EvidenceSynthesisLevel.SUPPORTED, emptyList(), emptyList(), "R", now)

        val result = engine.reconcileCycle(briefing)

        assertEquals(CommunicationReconciliationStatus.CHANGED, result.status)
        assertEquals(true, result.itemDiffs[0].stateChanged)
    }

    private fun createMockBriefing(id: String, signalId: String): IntelligenceBriefing {
        val now = Instant.now()
        val signal = Signal(
            id = signalId,
            title = "Title",
            summary = "Summary",
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.HIGH,
            significanceLevel = SignificanceOutcome.SIGNIFICANT,
            detectedAt = now,
            publishedAt = now,
            source = SignalSource(name = "Source")
        )
        val item = IntelligenceBriefingItem(
            id = "item-1",
            position = 0,
            communicationPackageId = signalId,
            intelligenceId = signalId,
            threadId = "t1",
            currentStateReference = "e1",
            changeReference = "c1",
            continuityReference = ContinuityLevel.CONTINUATION,
            significanceReference = SignificanceOutcome.SIGNIFICANT,
            priorityReference = AttentionLevel.IMPORTANT,
            evidenceReference = EvidenceSynthesisLevel.SUPPORTED,
            uncertaintyReference = SignalConfidence.HIGH,
            conflictReference = false,
            narrativeReference = "t1",
            provenanceReference = signalId,
            readiness = CommunicationReadinessLevel.READY,
            inclusionReason = "Reason"
        )
        val handoff = IntelligenceCommunicationHandoff(
            packageId = signalId,
            intelligenceId = signalId,
            threadId = "t1",
            communicationPackage = IntelligenceCommunicationPackage(
                intelligenceId = signalId,
                threadId = "t1",
                changeId = null,
                currentState = ReconstructedState("t1", "Sum", "e1", reconstructedAt = now),
                continuity = null,
                narrative = IntelligenceNarrativeSequence("t1", "Topic", emptyList()),
                significanceLevel = SignificanceOutcome.SIGNIFICANT,
                priority = AttentionLevel.IMPORTANT,
                evidenceSynthesis = EvidenceSynthesisResult("t1", EvidenceSynthesisLevel.SUPPORTED, emptyList(), emptyList(), "R", now),
                confidence = SignalConfidence.HIGH,
                conflicts = emptyList(),
                evidenceGap = EvidenceGapResult("t1", EvidenceGapLevel.NO_IDENTIFIED_GAP, "R", analyzedAt = now),
                relevance = null,
                freshness = null,
                communicationReadiness = CommunicationReadinessResult(CommunicationReadinessLevel.READY, "R", CommunicationSafetyReason.SATEISFIED, false, now),
                provenance = IntelligenceProvenance(signalId, signal, null, emptyList(), emptyList(), null, null, now),
                assembledAt = now
            ),
            authorizedAt = now
        )
        return IntelligenceBriefing(
            id = id,
            cycleId = "c1",
            generatedAt = now,
            status = BriefingStatus.READY,
            items = listOf(item),
            signals = mapOf(signalId to signal),
            handoffs = mapOf(signalId to handoff)
        )
    }
}
