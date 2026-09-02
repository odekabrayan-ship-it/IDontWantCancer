package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceEventTimeline
import com.idontwantcancer.app.domain.model.TimelineEntry
import com.idontwantcancer.app.domain.model.TimelineEntryType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class TimelineReconstructorTest {

    private val reconstructor = DefaultTimelineReconstructor()

    @Test
    fun `timeline should be sorted by event time first`() {
        val t1 = createEntry(id = "late", eventTime = Instant.ofEpochMilli(2000))
        val t2 = createEntry(id = "early", eventTime = Instant.ofEpochMilli(1000))
        
        val timeline = reconstructor.reconstruct("thread-1", "Topic", listOf(t1, t2))
        
        assertEquals("early", timeline.sortedEntries[0].id)
        assertEquals("late", timeline.sortedEntries[1].id)
    }

    @Test
    fun `timeline should fall back to publication time when event time is missing`() {
        val t1 = createEntry(id = "pub-late", eventTime = null, pubTime = Instant.ofEpochMilli(3000))
        val t2 = createEntry(id = "evt-early", eventTime = Instant.ofEpochMilli(1000))
        val t3 = createEntry(id = "pub-early", eventTime = null, pubTime = Instant.ofEpochMilli(2000))

        val timeline = reconstructor.reconstruct("thread-1", "Topic", listOf(t1, t2, t3))

        assertEquals("evt-early", timeline.sortedEntries[0].id)
        assertEquals("pub-early", timeline.sortedEntries[1].id)
        assertEquals("pub-late", timeline.sortedEntries[2].id)
    }

    @Test
    fun `deriveCurrentStateSummary should return last entry description if no terminal state exists`() {
        val t1 = createEntry(id = "1", desc = "State 1", eventTime = Instant.ofEpochMilli(1000))
        val t2 = createEntry(id = "2", desc = "State 2", eventTime = Instant.ofEpochMilli(2000))
        val timeline = reconstructor.reconstruct("thread-1", "Topic", listOf(t1, t2))

        val summary = reconstructor.deriveCurrentStateSummary(timeline)

        assertEquals("State 2", summary)
    }

    @Test
    fun `deriveCurrentStateSummary should respect terminal states like RESOLUTION`() {
        val t1 = createEntry(id = "1", desc = "Event", eventTime = Instant.ofEpochMilli(1000))
        val t2 = createEntry(id = "2", type = TimelineEntryType.RESOLUTION, desc = "Resolved", eventTime = Instant.ofEpochMilli(1500))
        val t3 = createEntry(id = "3", desc = "Late Noise", eventTime = Instant.ofEpochMilli(2000))
        
        val timeline = reconstructor.reconstruct("thread-1", "Topic", listOf(t1, t2, t3))

        val summary = reconstructor.deriveCurrentStateSummary(timeline)

        assertEquals("Resolved", summary)
    }

    private fun createEntry(
        id: String,
        type: TimelineEntryType = TimelineEntryType.MATERIAL_CHANGE,
        desc: String = "Description",
        eventTime: Instant? = null,
        pubTime: Instant? = null
    ) = TimelineEntry(
        id = id,
        threadId = "thread-1",
        type = type,
        description = desc,
        eventTime = eventTime,
        publicationTime = pubTime,
        ingestionTime = Instant.now()
    )
}
