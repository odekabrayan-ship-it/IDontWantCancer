package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceSupersessionEngineTest {

    private val engine = DefaultIntelligenceSupersessionEngine()

    @Test
    fun `correction supersedes previous material change`() {
        val e1 = createEntry(id = "old", type = TimelineEntryType.MATERIAL_CHANGE)
        val correction = createEntry(id = "corr", type = TimelineEntryType.CORRECTION)
        
        val relations = engine.detectSupersession(correction, listOf(e1))

        assertEquals(1, relations.size)
        assertEquals("old", relations[0].previousEntryId)
        assertEquals("corr", relations[0].supersedingEntryId)
        assertEquals(SupersessionType.CORRECTION, relations[0].type)
    }

    @Test
    fun `new regulatory action supersedes previous regulatory action`() {
        val e1 = createEntry(id = "reg1", type = TimelineEntryType.REGULATORY_ACTION)
        val e2 = createEntry(id = "reg2", type = TimelineEntryType.REGULATORY_ACTION)

        val relations = engine.detectSupersession(e2, listOf(e1))

        assertEquals(1, relations.size)
        assertEquals("reg1", relations[0].previousEntryId)
        assertEquals(SupersessionType.REPLACEMENT, relations[0].type)
    }

    @Test
    fun `retraction supersedes all previous entries in thread`() {
        val e1 = createEntry(id = "1")
        val e2 = createEntry(id = "2")
        val retraction = createEntry(id = "retract", type = TimelineEntryType.RETRACTION)

        val relations = engine.detectSupersession(retraction, listOf(e1, e2))

        assertEquals(2, relations.size)
        assertTrue(relations.any { it.previousEntryId == "1" && it.type == SupersessionType.RETRACTION })
        assertTrue(relations.any { it.previousEntryId == "2" && it.type == SupersessionType.RETRACTION })
    }

    @Test
    fun `newer timestamp alone does not create supersession`() {
        val e1 = createEntry(id = "1", type = TimelineEntryType.MATERIAL_CHANGE)
        val e2 = createEntry(id = "2", type = TimelineEntryType.MATERIAL_CHANGE)

        val relations = engine.detectSupersession(e2, listOf(e1))

        // MATERIAL_CHANGE does not automatically supersede previous MATERIAL_CHANGE
        assertTrue(relations.isEmpty())
    }

    private fun createEntry(
        id: String,
        type: TimelineEntryType = TimelineEntryType.INITIAL_OBSERVATION
    ) = TimelineEntry(
        id = id,
        threadId = "t1",
        type = type,
        description = "Desc",
        ingestionTime = Instant.now()
    )
}
