package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceBriefingExplanationEngineTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val engine = DefaultIntelligenceBriefingExplanationEngine(memory)

    @Test
    fun `valid signal produces correct structured explanation`() = runTest {
        val signal = createMockSignal(
            id = "sig-1",
            significance = SignificanceOutcome.HIGH_SIGNIFICANCE,
            confidence = SignalConfidence.HIGH
        )
        val item = createBriefingItem("sig-1")
        val entry = createTimelineEntry("sig-1", TimelineEntryType.REGULATORY_ACTION)
        
        coEvery { memory.getTimelineEntriesForThread("t1") } returns listOf(entry)

        val explanation = engine.explain(item, signal)

        assertEquals("An official regulatory or public health action has been taken.", explanation.whatChanged)
        assertEquals("This is a high-significance change that materially impacts the established understanding of this topic.", explanation.whyItMatters)
        assertEquals("This conclusion is supported by strong evidence from authoritative sources.", explanation.evidenceStatus)
    }

    @Test
    fun `unresolved conflict is reflected in explanation`() = runTest {
        val signal = createMockSignal(
            id = "sig-1",
            conflictStatus = ResolutionStatus.UNRESOLVED
        )
        val item = createBriefingItem("sig-1")
        
        coEvery { memory.getTimelineEntriesForThread("t1") } returns emptyList()

        val explanation = engine.explain(item, signal)

        assertTrue(explanation.conflictSummary!!.contains("differing information"))
        assertTrue(explanation.uncertainty!!.contains("Competing authoritative interpretations"))
    }

    private fun createMockSignal(
        id: String,
        significance: SignificanceOutcome = SignificanceOutcome.SIGNIFICANT,
        confidence: SignalConfidence = SignalConfidence.MODERATE,
        conflictStatus: ResolutionStatus? = null
    ) = Signal(
        id = id,
        title = "Title",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = confidence,
        significanceLevel = significance,
        conflictStatus = conflictStatus,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source")
    )

    private fun createBriefingItem(signalId: String) = IntelligenceBriefingItem(
        id = "item-1",
        position = 0,
        communicationPackageId = "p1",
        intelligenceId = signalId,
        threadId = "t1",
        currentStateReference = "e1",
        changeReference = "c1",
        continuityReference = ContinuityLevel.CONTINUATION,
        significanceReference = SignificanceOutcome.SIGNIFICANT,
        priorityReference = AttentionLevel.ROUTINE,
        evidenceReference = EvidenceSynthesisLevel.SUPPORTED,
        uncertaintyReference = SignalConfidence.MODERATE,
        conflictReference = false,
        narrativeReference = "t1",
        provenanceReference = signalId,
        uncertaintyModel = null,
        readiness = CommunicationReadinessLevel.READY,
        inclusionReason = "Reason"
    )

    private fun createTimelineEntry(signalId: String, type: TimelineEntryType) = TimelineEntry(
        id = "e1",
        threadId = "t1",
        type = type,
        description = "Desc",
        ingestionTime = Instant.now(),
        signalId = signalId
    )
}
