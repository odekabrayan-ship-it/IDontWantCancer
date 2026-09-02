package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.TimelineEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [TimelineEntryEntity].
 */
@Dao
interface TimelineEntryDao {
    @Query("SELECT * FROM timeline_entries WHERE threadId = :threadId")
    suspend fun getByThreadId(threadId: String): List<TimelineEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: TimelineEntryEntity)

    @Query("SELECT * FROM timeline_entries WHERE threadId = :threadId")
    fun getByThreadIdFlow(threadId: String): Flow<List<TimelineEntryEntity>>
}
