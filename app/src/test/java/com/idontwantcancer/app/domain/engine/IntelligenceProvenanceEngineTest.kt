package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.Instant

class IntelligenceProvenanceEngineTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val engine = DefaultIntelligenceProvenanceEngine(memory)

    @Test
    fun `provenance should trace from signal back to source material`() = runTest {
        val signalId = "sig-1"
        val materialId = "m-1"
        val threadId = "t-1"
        
        val signal = createMockSignal(signalId)
        val material = createMockMaterial(materialId)
        val thread = IntelligenceThread(threadId, "Topic", Instant.now(), Instant.now())
        val entry = TimelineEntry(
            id = "e-1",
            threadId = threadId,
            type = TimelineEntryType.MATERIAL_CHANGE,
            description = "Desc",
            ingestionTime = Instant.now(),
            sourceMaterialId = materialId,
            signalId = signalId
        )

        coEvery { memory.getSignalById(signalId) } returns signal
        coEvery { memory.findThreadByTopic("Title") } returns thread
        coEvery { memory.getTimelineEntriesForThread(threadId) } returns listOf(entry)
        coEvery { memory.getSourceMaterialById(materialId) } returns material
        coEvery { memory.getAllConsolidatedEvents() } returns emptyList()

        val provenance = engine.getSignalProvenance(signalId)

        assertEquals(signalId, provenance.signalId)
        assertEquals(1, provenance.sources.size)
        assertEquals(materialId, provenance.sources[0].contentId)
        assertNotNull(provenance.thread)
        assertEquals(1, provenance.timelineEntries.size)
    }

    private fun createMockSignal(id: String) = Signal(
        id = id,
        title = "Title",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = SignalConfidence.HIGH,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source")
    )

    private fun createMockMaterial(id: String) = SourceMaterial(
        sourceId = "s1",
        contentId = id,
        title = "Title",
        content = "Content",
        publishedAt = Instant.now(),
        contentHash = "hash"
    )
}
