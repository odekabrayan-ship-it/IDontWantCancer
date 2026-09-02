package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.ConflictEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [ConflictEntity].
 */
@Dao
interface ConflictDao {
    @Query("SELECT * FROM conflicts WHERE id = :conflictId")
    suspend fun getById(conflictId: String): ConflictEntity?

    @Query("SELECT * FROM conflicts WHERE topicIdentifier = :topicId")
    suspend fun getByTopic(topicId: String): List<ConflictEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(conflict: ConflictEntity)

    @Query("SELECT * FROM conflicts ORDER BY detectedAt DESC")
    fun getAllFlow(): Flow<List<ConflictEntity>>
}
