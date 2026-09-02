package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceBriefingAssemblyEngineTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val explanationEngine = mockk<IntelligenceBriefingExplanationEngine>(relaxed = true)
    private val synthesisEngine = mockk<IntelligenceEvidenceSynthesisEngine>(relaxed = true)
    private val gapEngine = mockk<IntelligenceEvidenceGapEngine>(relaxed = true)
    private val readinessEngine = mockk<IntelligenceCommunicationReadinessEngine>(relaxed = true)
    private val provenanceEngine = mockk<IntelligenceProvenanceEngine>(relaxed = true)
    private val narrativeEngine = mockk<IntelligenceNarrativeContinuityEngine>(relaxed = true)
    private val stateReconstructor = mockk<IntelligenceStateReconstructionEngine>(relaxed = true)
    private val freshnessEngine = mockk<IntelligenceFreshnessEngine>(relaxed = true)
    private val relevanceEngine = mockk<IntelligenceRelevanceEngine>(relaxed = true)
    private val continuityEngine = mockk<IntelligenceContinuityEngine>(relaxed = true)
    private val communicationAssemblyEngine = mockk<IntelligenceCommunicationAssemblyEngine>(relaxed = true)
    private val selectionEngine = mockk<IntelligenceBriefingSelectionEngine>(relaxed = true)
    private val contextEngine = mockk<IntelligenceBriefingContextEngine>(relaxed = true)
    private val uncertaintyEngine = mockk<IntelligenceUncertaintyEngine>(relaxed = true)
    private val handoffFactory = mockk<IntelligenceCommunicationHandoffFactory>(relaxed = true)
    private val orderingContract = mockk<IntelligenceCommunicationOrderingContract>(relaxed = true)
    private val selectionGate = mockk<IntelligenceCommunicationSelectionGate>(relaxed = true)
    private val reentryHandoffBoundary = mockk<IntelligenceReentryHandoffBoundary>(relaxed = true)
    private val acknowledgementBoundary = mockk<IntelligenceReentryAcknowledgementBoundary>(relaxed = true)
    private val reconciliationConsumptionBoundary = mockk<IntelligenceReentryReconciliationConsumptionBoundary>(relaxed = true)

    private val engine = DefaultIntelligenceBriefingAssemblyEngine(
        memory, explanationEngine, synthesisEngine, gapEngine, readinessEngine,
        provenanceEngine, narrativeEngine, stateReconstructor, freshnessEngine,
        relevanceEngine, continuityEngine, communicationAssemblyEngine, selectionEngine,
        contextEngine, uncertaintyEngine, handoffFactory, orderingContract, selectionGate,
        reentryHandoffBoundary, acknowledgementBoundary, reconciliationConsumptionBoundary
    )

    @Test
    fun `when no eligible signals exist, status is NO_MAJOR_CHANGES`() = runTest {
        val now = Instant.now()
        val signals = listOf(
            createPrioritizedSignal("sig1", AttentionLevel.ROUTINE)
        )
        
        coEvery { memory.findThreadByTopic(any()) } returns null
        coEvery { memory.getConflictsForTopic(any()) } returns emptyList()
        coEvery { memory.getTimelineEntriesForThread(any()) } returns emptyList()
        coEvery { memory.getSignalById(any()) } returns null

        // Mock selection engine to return empty selection
        every { selectionEngine.selectBriefing(any()) } returns IntelligenceBriefingSelection(
            id = "sel1",
            orderedSignalIds = emptyList(),
            selectedPackages = emptyMap(),
            excludedSignals = emptyMap(),
            selectedAt = now
        )
        
        every { selectionGate.selectEligible(any()) } returns IntelligenceCommunicationSelectionResult(
            selectedHandoffs = emptyList(),
            excludedHandoffs = emptyMap(),
            selectedAt = now
        )

        val briefing = engine.assembleBriefing(signals, now)

        assertEquals(BriefingStatus.NO_MAJOR_CHANGES, briefing.status)
        assertTrue(briefing.items.isEmpty())
        assertEquals("cycle-${now.toEpochMilli()}", briefing.cycleId)
    }

    private fun createPrioritizedSignal(
        id: String,
        attention: AttentionLevel,
        significance: SignificanceOutcome = SignificanceOutcome.SIGNIFICANT,
        title: String = "Title"
    ): PrioritizedSignal {
        val signal = Signal(
            id = id,
            title = title,
            summary = "Summary",
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.HIGH,
            significanceLevel = significance,
            detectedAt = Instant.now(),
            publishedAt = Instant.now(),
            source = SignalSource(name = "Source")
        )
        return PrioritizedSignal(signal, attention, 100)
    }
}
