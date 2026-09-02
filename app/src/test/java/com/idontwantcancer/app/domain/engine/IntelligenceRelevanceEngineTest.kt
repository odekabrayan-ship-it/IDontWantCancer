package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceRelevanceEngineTest {

    private val engine = DefaultIntelligenceRelevanceEngine()

    @Test
    fun `correctly scoped intelligence is relevant`() {
        val material = createMockMaterial(title = "Topic A")
        val thread = createMockThread(topic = "Topic A")

        val result = engine.evaluateRelevance(material, listOf(thread))

        assertEquals(RelevanceLevel.RELEVANT, result.level)
    }

    @Test
    fun `unrelated Thread is not relevant`() {
        val material = createMockMaterial(title = "Topic B")
        val thread = createMockThread(topic = "Topic A")

        val result = engine.evaluateRelevance(material, listOf(thread))

        assertEquals(RelevanceLevel.NOT_RELEVANT, result.level)
    }

    @Test
    fun `case insensitive matching for topic identifiers`() {
        val material = createMockMaterial(title = "topic a")
        val thread = createMockThread(topic = "TOPIC A")

        val result = engine.evaluateRelevance(material, listOf(thread))

        assertEquals(RelevanceLevel.RELEVANT, result.level)
    }

    private fun createMockMaterial(title: String) = SourceMaterial(
        sourceId = "s1",
        contentId = "m1",
        title = title,
        content = "Content",
        publishedAt = Instant.now(),
        contentHash = "hash"
    )

    private fun createMockThread(topic: String) = IntelligenceThread(
        id = "t1",
        topicIdentifier = topic,
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now()
    )
}
