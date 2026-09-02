package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceStateReconstructionEngineTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val engine = DefaultIntelligenceStateReconstructionEngine(memory)

    @Test
    fun `when timeline has single event, it defines the current state`() = runTest {
        val entry = createEntry(id = "1", desc = "Initial State")
        val timeline = IntelligenceEventTimeline("t1", "Topic", listOf(entry))
        
        coEvery { memory.getSupersessionRelationsForEntry(any()) } returns emptyList()

        val state = engine.reconstructCurrentState(timeline)

        assertEquals("Initial State", state.summary)
        assertEquals("1", state.effectiveEntryId)
    }

    @Test
    fun `when correction exists, it supersedes previous state in summary`() = runTest {
        val now = Instant.now()
        val e1 = createEntry(id = "1", desc = "Wrong State", ingestionTime = now.minusSeconds(100))
        val e2 = createEntry(id = "2", type = TimelineEntryType.CORRECTION, desc = "Corrected State", ingestionTime = now)
        val timeline = IntelligenceEventTimeline("t1", "Topic", listOf(e1, e2))

        val relation = SupersessionRelation(
            id = "r1",
            previousEntryId = "1",
            supersedingEntryId = "2",
            type = SupersessionType.CORRECTION,
            reason = "Correction",
            detectedAt = now
        )

        coEvery { memory.getSupersessionRelationsForEntry("1") } returns listOf(relation)
        coEvery { memory.getSupersessionRelationsForEntry("2") } returns listOf(relation)

        val state = engine.reconstructCurrentState(timeline)

        assertEquals("Corrected State", state.summary)
        assertEquals("2", state.effectiveEntryId)
    }

    @Test
    fun `reversal is reconstructed correctly as the primary state`() = runTest {
        val now = Instant.now()
        val e1 = createEntry(id = "1", desc = "A", ingestionTime = now.minusSeconds(200))
        val e2 = createEntry(id = "2", desc = "B", ingestionTime = now.minusSeconds(100))
        val e3 = createEntry(id = "3", type = TimelineEntryType.REVERSAL, desc = "Back to A", ingestionTime = now)
        val timeline = IntelligenceEventTimeline("t1", "Topic", listOf(e1, e2, e3))

        coEvery { memory.getSupersessionRelationsForEntry(any()) } returns emptyList()

        val state = engine.reconstructCurrentState(timeline)

        assertEquals("Back to A", state.summary)
    }

    @Test
    fun `evidence confidence from associated signal is preserved`() = runTest {
        val entry = createEntry(id = "1", signalId = "sig-1")
        val signal = createMockSignal(id = "sig-1", confidence = SignalConfidence.VERY_HIGH)
        coEvery { memory.getSignalById("sig-1") } returns signal
        coEvery { memory.getSupersessionRelationsForEntry(any()) } returns emptyList()
        
        val timeline = IntelligenceEventTimeline("t1", "Topic", listOf(entry))

        val state = engine.reconstructCurrentState(timeline)

        assertEquals(SignalConfidence.VERY_HIGH, state.confidence)
    }

    private fun createEntry(
        id: String,
        type: TimelineEntryType = TimelineEntryType.MATERIAL_CHANGE,
        desc: String = "Desc",
        ingestionTime: Instant = Instant.now(),
        signalId: String? = null
    ) = TimelineEntry(
        id = id,
        threadId = "t1",
        type = type,
        description = desc,
        ingestionTime = ingestionTime,
        signalId = signalId
    )

    private fun createMockSignal(id: String, confidence: SignalConfidence) = Signal(
        id = id,
        title = "Title",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = confidence,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source")
    )
}
