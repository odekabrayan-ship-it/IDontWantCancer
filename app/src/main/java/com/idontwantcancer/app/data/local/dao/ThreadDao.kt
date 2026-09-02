package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.ThreadEntity

/**
 * Data Access Object for [ThreadEntity].
 */
@Dao
interface ThreadDao {
    @Query("SELECT * FROM threads WHERE id = :threadId")
    suspend fun getById(threadId: String): ThreadEntity?

    @Query("SELECT * FROM threads WHERE topicIdentifier = :topicId")
    suspend fun getByTopic(topicId: String): ThreadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(thread: ThreadEntity)
}
