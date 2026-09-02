package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.data.datasource.IntelligenceDataSource
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.BriefingRepository
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import com.idontwantcancer.app.domain.repository.IntelligenceSourceRegistry
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceCycleCoordinatorTest {

    private val sourceRegistry = mockk<IntelligenceSourceRegistry>()
    private val intelligenceDataSource = mockk<IntelligenceDataSource>()
    private val memory = mockk<IntelligenceMemoryRepository>()
    private val briefingRepository = mockk<BriefingRepository>(relaxed = true)
    private val deduplicationEngine = mockk<IntelligenceDeduplicationEngine>()
    private val relevanceEngine = mockk<IntelligenceRelevanceEngine>()
    private val changeDiffEngine = mockk<ChangeDiffEngine>()
    private val significanceGate = mockk<IntelligenceSignificanceGate>()
    private val confidenceGate = mockk<EvidenceConfidenceGate>()
    private val contradictionEngine = mockk<IntelligenceContradictionResolutionEngine>()
    private val stateReconstructor = mockk<IntelligenceStateReconstructionEngine>()
    private val supersessionEngine = mockk<IntelligenceSupersessionEngine>()
    private val stateTransitionEngine = mockk<IntelligenceStateTransitionEngine>()
    private val continuityEngine = mockk<IntelligenceContinuityEngine>()
    private val reconciliationEngine = mockk<IntelligenceStateReconciliationEngine>()
    private val evidenceSynthesisEngine = mockk<IntelligenceEvidenceSynthesisEngine>()
    private val evidenceGapEngine = mockk<IntelligenceEvidenceGapEngine>()
    private val integrityEngine = mockk<IntelligenceLifecycleIntegrityEngine>()
    private val evidenceAssessor = mockk<EvidenceAssessmentEngine>()
    private val signalFormer = mockk<SignalFormationEngine>()
    private val prioritizer = mockk<IntelligencePrioritizationEngine>()
    private val briefingAssemblyEngine = mockk<IntelligenceBriefingAssemblyEngine>()

    private val coordinator = DefaultIntelligenceCycleCoordinator(
        sourceRegistry,
        intelligenceDataSource,
        memory,
        briefingRepository,
        deduplicationEngine,
        relevanceEngine,
        changeDiffEngine,
        significanceGate,
        confidenceGate,
        contradictionEngine,
        stateReconstructor,
        supersessionEngine,
        stateTransitionEngine,
        continuityEngine,
        reconciliationEngine,
        evidenceSynthesisEngine,
        evidenceGapEngine,
        integrityEngine,
        evidenceAssessor,
        signalFormer,
        prioritizer,
        briefingAssemblyEngine
    )

    @Test
    fun `when no sources are enabled, result is empty but valid`() = runTest {
        every { sourceRegistry.getEnabledSources() } returns emptyList()
        coEvery { memory.getAllSignals() } returns emptyList()
        coEvery { integrityEngine.verifyGlobalIntegrity() } returns IntelligenceIntegrityResult(true, emptyList(), Instant.now())
        every { prioritizer.prioritize(any()) } returns emptyList()
        coEvery { briefingAssemblyEngine.assembleBriefing(any(), any()) } returns createMockBriefing()

        val result = coordinator.runCycle()

        assertEquals(0, result.sourcesCheckedCount)
        assertEquals(0, result.signalsFormedCount)
        assertTrue(result.failures.isEmpty())
    }

    @Test
    fun `when source has new material, it is processed through the pipeline`() = runTest {
        val source = createMockSource("s1")
        val material = createMockMaterial("m1")
        val consolidated = createMockConsolidated("event-1", "m1")
        val change = createMockChange("c1")
        val assessment = createMockAssessment(change)
        val signal = createMockSignal("sig1")
        val decision = SignificanceDecision(SignificanceOutcome.SIGNIFICANT, "Reason", emptyList(), Instant.now())
        val confidence = ConfidenceDecision(SignalConfidence.HIGH, emptyList(), Instant.now())
        val reconstructed = ReconstructedState("t1", "Summary", "entry-1", SignalConfidence.HIGH, null, "c1", Instant.now())
        val entry = createMockTimelineEntry("entry-1", "t1")
        val relevance = IntelligenceRelevanceResult(RelevanceLevel.NOT_RELEVANT, "New Thread")
        val synthesis = EvidenceSynthesisResult("t1", EvidenceSynthesisLevel.SUPPORTED, listOf("sig1"), emptyList(), "Reason", Instant.now())
        val continuity = IntelligenceContinuityResult(ContinuityLevel.MATERIAL_CHANGE, "t1", "sig1", null, "Reason", Instant.now())
        val reconciliation = IntelligenceStateReconciliationResult("t1", ReconciliationClassification.MATERIALLY_CHANGED, null, "entry-1", ContinuityLevel.MATERIAL_CHANGE, null, Instant.now())

        every { sourceRegistry.getEnabledSources() } returns listOf(source)
        coEvery { intelligenceDataSource.fetchSourceMaterial("s1") } returns listOf(material)
        coEvery { memory.getAllConsolidatedEvents() } returns emptyList()
        every { deduplicationEngine.checkDeduplication(material, any()) } returns DeduplicationResult(DeduplicationType.UNIQUE_EVENT)
        every { deduplicationEngine.consolidate(material, null) } returns consolidated
        coEvery { memory.saveConsolidatedEvent(consolidated) } just Runs
        coEvery { memory.findThreadByTopic(any()) } returns null
        every { relevanceEngine.evaluateRelevance(any(), any()) } returns relevance
        coEvery { memory.saveThread(any()) } just Runs
        coEvery { memory.getSignalById(any()) } returns null
        coEvery { memory.getPreviousMaterial("m1") } returns null
        coEvery { changeDiffEngine.detectChanges(material, null, any(), null) } returns listOf(change)
        coEvery { evidenceAssessor.assess(any(), any(), any()) } returns assessment
        every { significanceGate.evaluate(any(), any(), any()) } returns decision
        coEvery { memory.getAllSignals() } returns emptyList()
        coEvery { contradictionEngine.analyzeContradictions(any(), any(), any()) } returns emptyList()
        every { confidenceGate.evaluate(any(), any(), any()) } returns confidence
        coEvery { signalFormer.formSignal(any(), any(), any(), any(), any()) } returns SignalFormationResult.SignalCreated(signal)
        coEvery { memory.saveSignal(signal) } just Runs
        coEvery { memory.saveSourceMaterial(material) } just Runs
        coEvery { memory.saveTimelineEntry(any()) } just Runs
        coEvery { memory.getTimelineEntriesForThread(any()) } returns listOf(entry)
        every { supersessionEngine.detectSupersession(any(), any()) } returns emptyList()
        coEvery { memory.saveSupersessionRelation(any()) } just Runs
        every { evidenceSynthesisEngine.synthesizeEvidence(any(), any(), any()) } returns synthesis
        every { evidenceGapEngine.analyzeGaps(any(), any(), any()) } returns mockk(relaxed = true)
        every { stateTransitionEngine.evaluateTransition(any(), any(), any(), any(), any()) } returns null
        every { continuityEngine.evaluateContinuity(any(), any(), any(), any(), any()) } returns continuity
        every { reconciliationEngine.reconcileState(any(), any(), any(), any(), any()) } returns reconciliation
        coEvery { stateReconstructor.reconstructCurrentState(any()) } returns reconstructed
        coEvery { integrityEngine.verifyGlobalIntegrity() } returns IntelligenceIntegrityResult(true, emptyList(), Instant.now())
        every { prioritizer.prioritize(any()) } returns emptyList()
        coEvery { briefingAssemblyEngine.assembleBriefing(any(), any()) } returns createMockBriefing()

        val result = coordinator.runCycle()

        if (result.failures.isNotEmpty()) {
            println("Pipeline failures: ${result.failures}")
        }

        assertEquals(1, result.sourcesCheckedCount)
        assertEquals(1, result.newSourceItemsCount)
        assertEquals(1, result.signalsFormedCount)
        assertTrue(result.failures.isEmpty())
    }

    private fun createMockSource(id: String) = IntelligenceSource(
        id = id,
        name = "Source $id",
        type = IntelligenceSourceType.PUBLIC_HEALTH,
        authority = SourceAuthority.INTERNATIONAL,
        reliability = SourceReliability(
            trustLevel = SourceTrustLevel.HIGH,
            isTransparent = true,
            updateReliability = SourceTrustLevel.HIGH
        )
    )

    private fun createMockMaterial(id: String) = SourceMaterial(
        sourceId = "s1",
        contentId = id,
        title = "Title",
        content = "Content",
        publishedAt = Instant.now(),
        contentHash = "hash"
    )

    private fun createMockConsolidated(id: String, materialId: String) = ConsolidatedEvent(
        id = id,
        topicIdentifier = "Title",
        sourceMaterialIds = listOf(materialId),
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now()
    )

    private fun createMockChange(id: String) = DetectedChange(
        id = id,
        type = ChangeType.NEW_INFORMATION,
        sourceId = "s1",
        contentId = "m1",
        description = "Change",
        detectedAt = Instant.now()
    )

    private fun createMockAssessment(change: DetectedChange) = EvidenceAssessment(
        change = change,
        strength = EvidenceStrength.HIGH,
        factors = emptyList(),
        assessedAt = Instant.now()
    )

    private fun createMockSignal(id: String) = Signal(
        id = id,
        title = "Signal",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = SignalConfidence.HIGH,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source")
    )

    private fun createMockTimelineEntry(id: String, threadId: String) = TimelineEntry(
        id = id,
        threadId = threadId,
        type = TimelineEntryType.INITIAL_OBSERVATION,
        description = "Initial",
        ingestionTime = Instant.now()
    )

    private fun createMockBriefing() = IntelligenceBriefing(
        id = "b1",
        cycleId = "c1",
        generatedAt = Instant.now(),
        status = BriefingStatus.READY,
        items = emptyList(),
        signals = emptyMap()
    )
}
