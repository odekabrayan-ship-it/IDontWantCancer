package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.ConsolidatedEventEntity

/**
 * Data Access Object for [ConsolidatedEventEntity].
 */
@Dao
interface ConsolidatedEventDao {
    @Query("SELECT * FROM consolidated_events WHERE id = :eventId")
    suspend fun getById(eventId: String): ConsolidatedEventEntity?

    @Query("SELECT * FROM consolidated_events")
    suspend fun getAll(): List<ConsolidatedEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(event: ConsolidatedEventEntity)
}
