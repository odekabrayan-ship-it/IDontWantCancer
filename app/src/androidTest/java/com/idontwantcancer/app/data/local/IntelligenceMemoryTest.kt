package com.idontwantcancer.app.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idontwantcancer.app.data.local.dao.SignalDao
import com.idontwantcancer.app.data.local.dao.SourceMaterialDao
import com.idontwantcancer.app.data.local.entity.SignalEntity
import com.idontwantcancer.app.data.local.entity.SourceMaterialEntity
import com.idontwantcancer.app.domain.model.IntelligenceLifecycle
import com.idontwantcancer.app.domain.model.SignalCategory
import com.idontwantcancer.app.domain.model.SignalConfidence
import com.idontwantcancer.app.domain.model.SignalImportance
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class IntelligenceMemoryTest {

    private lateinit var db: AppDatabase
    private lateinit var signalDao: SignalDao
    private lateinit var sourceMaterialDao: SourceMaterialDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        signalDao = db.signalDao()
        sourceMaterialDao = db.sourceMaterialDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadSignal() = runBlocking {
        val signal = SignalEntity(
            id = "test-id",
            title = "Test Signal",
            summary = "Test Summary",
            significance = "Test Significance",
            explanation = "Test Explanation",
            category = SignalCategory.RESEARCH,
            importance = SignalImportance.HIGH,
            confidence = SignalConfidence.VERY_HIGH,
            detectedAt = 123456789L,
            publishedAt = 123456780L,
            recommendedAction = "Test Action",
            sourceName = "Test Source",
            sourceUrl = "http://test.com",
            lifecycle = IntelligenceLifecycle.DETECTED,
            firstObservedAt = 123456789L,
            lastUpdatedAt = 123456789L
        )
        signalDao.upsert(signal)
        val retrieved = signalDao.getById("test-id")
        assertNotNull(retrieved)
        assertEquals(signal.title, retrieved?.title)
    }

    @Test
    fun writeAndReadSourceMaterial() = runBlocking {
        val material = SourceMaterialEntity(
            contentId = "material-1",
            sourceId = "source-1",
            title = "Material Title",
            content = "Material Content",
            publishedAt = 100L,
            updatedAt = null,
            canonicalUrl = null,
            contentHash = "hash-123",
            firstObservedAt = 200L,
            lastProcessedAt = 300L
        )
        sourceMaterialDao.upsert(material)
        val retrieved = sourceMaterialDao.getById("material-1")
        assertNotNull(retrieved)
        assertEquals(material.contentHash, retrieved?.contentHash)
    }
}
