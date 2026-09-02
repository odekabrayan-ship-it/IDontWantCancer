package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class ChangeDiffEngineTest {

    private val engine = DefaultChangeDiffEngine()

    @Test
    fun `when content is identical, detect no change`() = runTest {
        val material = createMockMaterial(hash = "same")
        val previous = createMockMaterial(hash = "same")

        val result = engine.detectChanges(material, previous, null, null)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `when no thread exists, detect NEW_INFORMATION`() = runTest {
        val material = createMockMaterial(hash = "new")

        val result = engine.detectChanges(material, null, null, null)

        assertEquals(1, result.size)
        assertEquals(ChangeType.NEW_INFORMATION, result[0].type)
    }

    @Test
    fun `when content changes in existing thread, detect MATERIAL_CHANGE`() = runTest {
        val material = createMockMaterial(hash = "updated")
        val previous = createMockMaterial(hash = "old")
        val thread = createMockThread()

        val result = engine.detectChanges(material, previous, thread, null)

        assertEquals(1, result.size)
        assertEquals(ChangeType.MATERIAL_CHANGE, result[0].type)
    }

    @Test
    fun `when content contains reversal language, detect REVERSAL`() = runTest {
        val material = createMockMaterial(content = "This recommendation no longer recommended.", hash = "new")
        val previous = createMockMaterial(content = "Previous text", hash = "old")
        val thread = createMockThread()

        val result = engine.detectChanges(material, previous, thread, null)

        assertEquals(1, result.size)
        assertEquals(ChangeType.REVERSAL, result[0].type)
    }

    @Test
    fun `when content contains correction language, detect CORRECTION`() = runTest {
        val material = createMockMaterial(content = "An erratum has been published.", hash = "new")
        val previous = createMockMaterial(content = "Original text", hash = "old")
        val thread = createMockThread()

        val result = engine.detectChanges(material, previous, thread, null)

        assertEquals(1, result.size)
        assertEquals(ChangeType.CORRECTION, result[0].type)
    }

    private fun createMockMaterial(
        hash: String = "hash",
        content: String = "Test content"
    ) = SourceMaterial(
        sourceId = "s1",
        contentId = "m1",
        title = "Title",
        content = content,
        publishedAt = Instant.now(),
        contentHash = hash
    )

    private fun createMockThread() = IntelligenceThread(
        id = "t1",
        topicIdentifier = "Title",
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now()
    )
}
