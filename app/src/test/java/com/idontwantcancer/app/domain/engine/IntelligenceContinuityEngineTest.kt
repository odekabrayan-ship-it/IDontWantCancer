package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceContinuityEngineTest {

    private val engine = DefaultIntelligenceContinuityEngine()

    @Test
    fun `initial event is CONTINUATION if no previous state exists`() {
        val entry = createEntry(id = "1")
        val thread = createThread("t1")

        val result = engine.evaluateContinuity(entry, null, null, thread, emptyList())

        assertEquals(ContinuityLevel.CONTINUATION, result.level)
    }

    @Test
    fun `genuine opposite state is classified as reversal`() {
        val previous = createMockState(entryId = "old")
        val entry = createEntry(id = "new", type = TimelineEntryType.REVERSAL)
        val thread = createThread("t1")

        val result = engine.evaluateContinuity(entry, null, previous, thread, emptyList())

        assertEquals(ContinuityLevel.REVERSAL, result.level)
    }

    @Test
    fun `authoritative replacement is classified as material change`() {
        val previous = createMockState(entryId = "old")
        val entry = createEntry(id = "new", type = TimelineEntryType.REGULATORY_ACTION)
        val thread = createThread("t1")
        val relation = SupersessionRelation("r1", "old", "new", SupersessionType.REPLACEMENT, "Update", Instant.now())

        val result = engine.evaluateContinuity(entry, null, previous, thread, listOf(relation))

        assertEquals(ContinuityLevel.MATERIAL_CHANGE, result.level)
    }

    @Test
    fun `evidence upgrade is classified as update`() {
        val previous = createMockState(entryId = "old")
        val entry = createEntry(id = "new", type = TimelineEntryType.EVIDENCE_UPGRADE)
        val thread = createThread("t1")

        val result = engine.evaluateContinuity(entry, null, previous, thread, emptyList())

        assertEquals(ContinuityLevel.UPDATE, result.level)
    }

    @Test
    fun `resolved thread reactivation is detected`() {
        val previous = createMockState(entryId = "old")
        val entry = createEntry(id = "new", type = TimelineEntryType.MATERIAL_CHANGE)
        val thread = createThread("t1", status = "Resolved topic")

        val result = engine.evaluateContinuity(entry, null, previous, thread, emptyList())

        assertEquals(ContinuityLevel.REACTIVATED, result.level)
    }

    private fun createEntry(
        id: String,
        type: TimelineEntryType = TimelineEntryType.MATERIAL_CHANGE
    ) = TimelineEntry(
        id = id,
        threadId = "t1",
        type = type,
        description = "Desc",
        ingestionTime = Instant.now()
    )

    private fun createThread(id: String, status: String? = null) = IntelligenceThread(
        id = id,
        topicIdentifier = "Topic",
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now(),
        currentStatus = status
    )

    private fun createMockState(entryId: String) = ReconstructedState(
        threadId = "t1",
        summary = "Summary",
        effectiveEntryId = entryId,
        confidence = SignalConfidence.HIGH,
        conflictStatus = ResolutionStatus.RESOLVED,
        reconstructedAt = Instant.now()
    )
}
