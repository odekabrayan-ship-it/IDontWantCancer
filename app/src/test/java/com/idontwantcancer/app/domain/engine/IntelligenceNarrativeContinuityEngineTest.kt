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

class IntelligenceNarrativeContinuityEngineTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val engine = DefaultIntelligenceNarrativeContinuityEngine(memory)

    @Test
    fun `initial intelligence creates the appropriate INITIAL_DETECTION event`() = runTest {
        val thread = createThread("t1")
        val entry = createEntry("e1", type = TimelineEntryType.INITIAL_OBSERVATION)
        val transition = createTransition("tr1", entry.id, type = IntelligenceStateTransitionType.INITIALIZED)

        coEvery { memory.getTimelineEntriesForThread("t1") } returns listOf(entry)
        coEvery { memory.getStateTransitionsForThread("t1") } returns listOf(transition)
        coEvery { memory.getSupersessionRelationsForEntry("e1") } returns emptyList()
        coEvery { memory.getConflictsForTopic(any()) } returns emptyList()

        val narrative = engine.reconstructNarrative(thread)

        assertEquals(1, narrative.events.size)
        assertEquals(IntelligenceNarrativeEventCategory.INITIAL_DETECTION, narrative.events[0].category)
        assertEquals("e1", narrative.events[0].timelineEntryId)
    }

    @Test
    fun `late-arriving information is placed correctly in chronological sequence`() = runTest {
        val thread = createThread("t1")
        val e1 = createEntry("e1", eventTime = Instant.ofEpochMilli(1000))
        val e2 = createEntry("e2", eventTime = Instant.ofEpochMilli(3000))
        val eLate = createEntry("e3", eventTime = Instant.ofEpochMilli(2000)) // Late arrival but logically between e1 and e2

        coEvery { memory.getTimelineEntriesForThread("t1") } returns listOf(e1, e2, eLate)
        coEvery { memory.getStateTransitionsForThread("t1") } returns emptyList()
        coEvery { memory.getSupersessionRelationsForEntry(any()) } returns emptyList()
        coEvery { memory.getConflictsForTopic(any()) } returns emptyList()

        val narrative = engine.reconstructNarrative(thread)

        assertEquals(3, narrative.sortedEvents.size)
        assertEquals("e1", narrative.sortedEvents[0].timelineEntryId)
        assertEquals("e3", narrative.sortedEvents[1].timelineEntryId)
        assertEquals("e2", narrative.sortedEvents[2].timelineEntryId)
    }

    @Test
    fun `material change is represented correctly`() = runTest {
        val thread = createThread("t1")
        val entry = createEntry("e1", type = TimelineEntryType.MATERIAL_CHANGE)

        coEvery { memory.getTimelineEntriesForThread("t1") } returns listOf(entry)
        coEvery { memory.getStateTransitionsForThread("t1") } returns emptyList()
        coEvery { memory.getSupersessionRelationsForEntry("e1") } returns emptyList()
        coEvery { memory.getConflictsForTopic(any()) } returns emptyList()

        val narrative = engine.reconstructNarrative(thread)

        assertEquals(IntelligenceNarrativeEventCategory.MATERIAL_CHANGE, narrative.events[0].category)
    }

    private fun createThread(id: String) = IntelligenceThread(
        id = id,
        topicIdentifier = "Topic",
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now()
    )

    private fun createEntry(
        id: String,
        type: TimelineEntryType = TimelineEntryType.MATERIAL_CHANGE,
        eventTime: Instant? = null
    ) = TimelineEntry(
        id = id,
        threadId = "t1",
        type = type,
        description = "Desc",
        eventTime = eventTime,
        ingestionTime = Instant.now()
    )

    private fun createTransition(
        id: String,
        triggerId: String,
        type: IntelligenceStateTransitionType
    ) = IntelligenceStateTransition(
        id = id,
        threadId = "t1",
        previousStateEntryId = null,
        resultingStateEntryId = triggerId,
        type = type,
        triggeringEventId = triggerId,
        effectiveAt = Instant.now(),
        reason = "Reason"
    )
}
