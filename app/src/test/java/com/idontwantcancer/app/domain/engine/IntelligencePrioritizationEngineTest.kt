package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligencePrioritizationEngineTest {

    private val engine = DefaultIntelligencePrioritizationEngine()

    @Test
    fun `when list is empty, return empty list`() {
        val result = engine.prioritize(emptyList())
        assertEquals(0, result.size)
    }

    @Test
    fun `higher importance signal should be before lower importance`() {
        val lowSignal = createMockSignal(id = "low", importance = SignalImportance.LOW)
        val highSignal = createMockSignal(id = "high", importance = SignalImportance.CRITICAL)

        val result = engine.prioritize(listOf(lowSignal, highSignal))

        assertEquals(2, result.size)
        assertEquals("high", result[0].signal.id)
        assertEquals(AttentionLevel.IMMEDIATE, result[0].attentionLevel)
        assertEquals("low", result[1].signal.id)
        assertEquals(AttentionLevel.ARCHIVE, result[1].attentionLevel)
    }

    @Test
    fun `higher confidence signal should be prioritized within same attention level`() {
        val lowConf = createMockSignal(id = "lowConf", importance = SignalImportance.HIGH, confidence = SignalConfidence.LOW)
        val highConf = createMockSignal(id = "highConf", importance = SignalImportance.HIGH, confidence = SignalConfidence.VERY_HIGH)

        val result = engine.prioritize(listOf(lowConf, highConf))

        assertEquals("highConf", result[0].signal.id)
        assertEquals("lowConf", result[1].signal.id)
    }

    @Test
    fun `more recent signal should be prioritized if all other factors are equal`() {
        val now = Instant.now()
        val oldSignal = createMockSignal(id = "old", publishedAt = now.minusSeconds(3600))
        val newSignal = createMockSignal(id = "new", publishedAt = now)

        val result = engine.prioritize(listOf(oldSignal, newSignal))

        assertEquals("new", result[0].signal.id)
        assertEquals("old", result[1].signal.id)
    }

    @Test
    fun `prioritization should be deterministic`() {
        val signals = listOf(
            createMockSignal(id = "b"),
            createMockSignal(id = "a"),
            createMockSignal(id = "c")
        )

        val result1 = engine.prioritize(signals)
        val result2 = engine.prioritize(signals)

        assertEquals(result1.map { it.signal.id }, result2.map { it.signal.id })
    }

    private fun createMockSignal(
        id: String,
        importance: SignalImportance = SignalImportance.MODERATE,
        confidence: SignalConfidence = SignalConfidence.MODERATE,
        publishedAt: Instant = Instant.now()
    ) = Signal(
        id = id,
        title = "Title $id",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = importance,
        confidence = confidence,
        detectedAt = Instant.now(),
        publishedAt = publishedAt,
        source = SignalSource(name = "Test Source")
    )
}
