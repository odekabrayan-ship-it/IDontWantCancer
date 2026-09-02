package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class IntelligenceFreshnessEngineTest {

    private val engine = DefaultIntelligenceFreshnessEngine()

    @Test
    fun `recent intelligence is CURRENT`() {
        val now = Instant.now()
        val entry = createEntry("e1", eventTime = now.minus(1, ChronoUnit.DAYS))
        val timeline = IntelligenceEventTimeline("t1", "Topic", listOf(entry))
        val thread = createThread("t1")

        val status = engine.evaluateFreshness(entry, timeline, thread)

        assertEquals(FreshnessLevel.CURRENT, status.level)
    }

    @Test
    fun `superseded intelligence is STALE`() {
        val now = Instant.now()
        val e1 = createEntry("e1", eventTime = now.minus(10, ChronoUnit.DAYS), type = TimelineEntryType.MATERIAL_CHANGE)
        val e2 = createEntry("e2", eventTime = now.minus(5, ChronoUnit.DAYS), type = TimelineEntryType.REGULATORY_ACTION)
        
        val timeline = IntelligenceEventTimeline("t1", "Topic", listOf(e1, e2))
        val thread = createThread("t1")

        val status = engine.evaluateFreshness(e1, timeline, thread)

        assertEquals(FreshnessLevel.STALE, status.level)
        assertEquals("e2", status.supersededByEntryId)
    }

    @Test
    fun `resolved topic makes earlier info HISTORICAL`() {
        val now = Instant.now()
        val e1 = createEntry("e1", eventTime = now.minus(10, ChronoUnit.DAYS))
        val e2 = createEntry("e2", eventTime = now.minus(1, ChronoUnit.DAYS), type = TimelineEntryType.RESOLUTION)
        
        val timeline = IntelligenceEventTimeline("t1", "Topic", listOf(e1, e2))
        val thread = createThread("t1")

        val s1 = engine.evaluateFreshness(e1, timeline, thread)
        val s2 = engine.evaluateFreshness(e2, timeline, thread)

        assertEquals(FreshnessLevel.HISTORICAL, s1.level)
        assertEquals(FreshnessLevel.CURRENT, s2.level) // The resolution itself is the current state
    }

    @Test
    fun `regulatory action stays CURRENT longer than general evidence`() {
        val now = Instant.now()
        // 200 days old
        val time = now.minus(200, ChronoUnit.DAYS)
        
        val eResearch = createEntry("e1", eventTime = time, type = TimelineEntryType.MATERIAL_CHANGE)
        val eReg = createEntry("e2", eventTime = time, type = TimelineEntryType.REGULATORY_ACTION)
        
        val t1 = IntelligenceEventTimeline("t1", "Topic", listOf(eResearch))
        val t2 = IntelligenceEventTimeline("t2", "Topic", listOf(eReg))
        val thread = createThread("t1")

        val sResearch = engine.evaluateFreshness(eResearch, t1, thread)
        val sReg = engine.evaluateFreshness(eReg, t2, thread)

        // Research ages to AGING after 180 days
        assertEquals(FreshnessLevel.AGING, sResearch.level)
        // Regulatory stays CURRENT for up to 365 days
        assertEquals(FreshnessLevel.CURRENT, sReg.level)
    }

    private fun createEntry(
        id: String,
        eventTime: Instant? = null,
        type: TimelineEntryType = TimelineEntryType.MATERIAL_CHANGE
    ) = TimelineEntry(
        id = id,
        threadId = "t1",
        type = type,
        description = "Desc",
        eventTime = eventTime,
        ingestionTime = Instant.now()
    )

    private fun createThread(id: String) = IntelligenceThread(
        id = id,
        topicIdentifier = "Topic",
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now()
    )
}
