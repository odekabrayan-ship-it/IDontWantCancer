package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.SignalEntity
import com.idontwantcancer.app.domain.model.IntelligenceLifecycle
import com.idontwantcancer.app.domain.model.SignalCategory
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [SignalEntity].
 */
@Dao
interface SignalDao {
    @Query("SELECT * FROM signals WHERE id = :signalId")
    suspend fun getById(signalId: String): SignalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(signal: SignalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(signals: List<SignalEntity>)

    @Query("SELECT * FROM signals WHERE lifecycle = :lifecycle ORDER BY publishedAt DESC")
    fun getByLifecycleFlow(lifecycle: IntelligenceLifecycle): Flow<List<SignalEntity>>

    @Query("UPDATE signals SET lifecycle = :lifecycle, lastUpdatedAt = :timestamp WHERE id = :signalId")
    suspend fun updateLifecycle(signalId: String, lifecycle: IntelligenceLifecycle, timestamp: Long)

    @Query("SELECT * FROM signals ORDER BY publishedAt DESC")
    fun getAllFlow(): Flow<List<SignalEntity>>

    @Query("SELECT * FROM signals WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%' OR affectedIngredientsJson LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR investigatedClaim LIKE '%' || :query || '%' ORDER BY publishedAt DESC")
    suspend fun search(query: String): List<SignalEntity>

    @Query("SELECT * FROM signals WHERE category IN (:categories) ORDER BY publishedAt DESC")
    fun getByCategoriesFlow(categories: List<SignalCategory>): Flow<List<SignalEntity>>

    @Query("UPDATE signals SET isActionTaken = :isTaken WHERE id = :id")
    suspend fun updateActionTakenStatus(id: String, isTaken: Boolean)

    @Query("UPDATE signals SET isWatched = :isWatched WHERE id = :id")
    suspend fun updateWatchStatus(id: String, isWatched: Boolean)
}
