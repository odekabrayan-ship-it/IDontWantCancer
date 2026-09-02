package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceLifecycleIntegrityEngineTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val engine = DefaultIntelligenceLifecycleIntegrityEngine(memory)

    @Test
    fun `valid intelligence graph produces no violations`() = runTest {
        val threadId = "t1"
        val signalId = "s1"
        val thread = IntelligenceThread(threadId, "Topic", Instant.now(), Instant.now(), signalIds = listOf(signalId))
        val signal = createMockSignal(signalId)

        coEvery { memory.getThreadById(threadId) } returns thread
        coEvery { memory.getSignalById(signalId) } returns signal
        coEvery { memory.getTimelineEntriesForThread(threadId) } returns emptyList()

        val result = engine.verifyThreadIntegrity(threadId)

        assertTrue(result.isValid)
        assertTrue(result.violations.isEmpty())
    }

    @Test
    fun `missing thread reference is detected as error`() = runTest {
        val threadId = "unknown"
        coEvery { memory.getThreadById(threadId) } returns null

        val result = engine.verifyThreadIntegrity(threadId)

        assertFalse(result.isValid)
        assertEquals(1, result.violations.size)
        assertEquals(IntegrityViolationSeverity.ERROR, result.violations[0].severity)
        assertEquals(IntegrityViolationCategory.MISSING_REFERENCE, result.violations[0].category)
    }

    @Test
    fun `missing Signal reference in thread is detected`() = runTest {
        val threadId = "t1"
        val signalId = "missing"
        val thread = IntelligenceThread(threadId, "Topic", Instant.now(), Instant.now(), signalIds = listOf(signalId))

        coEvery { memory.getThreadById(threadId) } returns thread
        coEvery { memory.getSignalById(signalId) } returns null
        coEvery { memory.getTimelineEntriesForThread(threadId) } returns emptyList()

        val result = engine.verifyThreadIntegrity(threadId)

        assertFalse(result.isValid)
        assertTrue(result.violations.any { it.category == IntegrityViolationCategory.MISSING_REFERENCE })
    }

    @Test
    fun `invalid TimelineEntry relationship is detected`() = runTest {
        val threadId = "t1"
        val thread = IntelligenceThread(threadId, "Topic", Instant.now(), Instant.now(), signalIds = emptyList())
        val entry = TimelineEntry(
            id = "e1",
            threadId = threadId,
            type = TimelineEntryType.MATERIAL_CHANGE,
            description = "Desc",
            ingestionTime = Instant.now(),
            signalId = "unassigned-signal"
        )

        coEvery { memory.getThreadById(threadId) } returns thread
        coEvery { memory.getTimelineEntriesForThread(threadId) } returns listOf(entry)
        coEvery { memory.getSupersessionRelationsForEntry("e1") } returns emptyList()

        val result = engine.verifyThreadIntegrity(threadId)

        assertTrue(result.violations.any { it.category == IntegrityViolationCategory.INVALID_RELATIONSHIP })
    }

    @Test
    fun `supersession relation outside thread context is detected as error`() = runTest {
        val threadId = "t1"
        val thread = IntelligenceThread(threadId, "Topic", Instant.now(), Instant.now())
        val entry = TimelineEntry(id = "e1", threadId = threadId, type = TimelineEntryType.MATERIAL_CHANGE, description = "D", ingestionTime = Instant.now())
        
        // Relation referencing entry outside of t1's timeline entries
        val relation = SupersessionRelation("r1", "e1", "outside-entry", SupersessionType.REPLACEMENT, "Reason", Instant.now())

        coEvery { memory.getThreadById(threadId) } returns thread
        coEvery { memory.getTimelineEntriesForThread(threadId) } returns listOf(entry)
        coEvery { memory.getSupersessionRelationsForEntry("e1") } returns listOf(relation)

        val result = engine.verifyThreadIntegrity(threadId)

        assertFalse(result.isValid)
        assertTrue(result.violations.any { 
            it.category == IntegrityViolationCategory.INVALID_RELATIONSHIP && 
            it.severity == IntegrityViolationSeverity.ERROR 
        })
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
}
