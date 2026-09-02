package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceDeduplicationEngineTest {

    private val engine = DefaultIntelligenceDeduplicationEngine()

    @Test
    fun `when contentId is already in an existing event, detect DUPLICATE_EVENT`() {
        val material = createMockMaterial(contentId = "id-123")
        val existingEvent = createMockEvent(id = "event-1", sourceMaterialIds = listOf("id-123"))

        val result = engine.checkDeduplication(material, listOf(existingEvent))

        assertEquals(DeduplicationType.DUPLICATE_EVENT, result.type)
        assertEquals("event-1", result.existingEventId)
    }

    @Test
    fun `when topic and timing are identical, detect POSSIBLE_DUPLICATE`() {
        val now = Instant.now()
        val material = createMockMaterial(title = "Title A", publishedAt = now)
        val existingEvent = createMockEvent(id = "event-1", topic = "Title A", firstDetectedAt = now)

        val result = engine.checkDeduplication(material, listOf(existingEvent))

        assertEquals(DeduplicationType.POSSIBLE_DUPLICATE, result.type)
        assertEquals("event-1", result.existingEventId)
    }

    @Test
    fun `when material is unique, detect UNIQUE_EVENT`() {
        val material = createMockMaterial(contentId = "new-id", title = "New Title")
        val existingEvent = createMockEvent(id = "event-1", topic = "Old Title")

        val result = engine.checkDeduplication(material, listOf(existingEvent))

        assertEquals(DeduplicationType.UNIQUE_EVENT, result.type)
    }

    @Test
    fun `consolidate should add sourceMaterialId to existing event`() {
        val material = createMockMaterial(contentId = "source-2")
        val existingEvent = createMockEvent(id = "event-1", sourceMaterialIds = listOf("source-1"))

        val consolidated = engine.consolidate(material, existingEvent)

        assertEquals(2, consolidated.sourceMaterialIds.size)
        assertTrue(consolidated.sourceMaterialIds.contains("source-1"))
        assertTrue(consolidated.sourceMaterialIds.contains("source-2"))
    }

    @Test
    fun `consolidation should be idempotent for identical material`() {
        val material = createMockMaterial(contentId = "source-1")
        val existingEvent = createMockEvent(id = "event-1", sourceMaterialIds = listOf("source-1"))

        val consolidated = engine.consolidate(material, existingEvent)

        assertEquals(1, consolidated.sourceMaterialIds.size)
    }

    private fun createMockMaterial(
        contentId: String = "m1",
        title: String = "Title",
        publishedAt: Instant = Instant.now()
    ) = SourceMaterial(
        sourceId = "s1",
        contentId = contentId,
        title = title,
        content = "Content",
        publishedAt = publishedAt,
        contentHash = "hash"
    )

    private fun createMockEvent(
        id: String,
        topic: String = "Title",
        sourceMaterialIds: List<String> = emptyList(),
        firstDetectedAt: Instant = Instant.now()
    ) = ConsolidatedEvent(
        id = id,
        topicIdentifier = topic,
        sourceMaterialIds = sourceMaterialIds,
        firstDetectedAt = firstDetectedAt,
        lastUpdatedAt = Instant.now()
    )
}
