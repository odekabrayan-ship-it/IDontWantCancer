package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.StateTransitionEntity

/**
 * Data Access Object for [StateTransitionEntity].
 */
@Dao
interface StateTransitionDao {
    @Query("SELECT * FROM state_transitions WHERE threadId = :threadId ORDER BY effectiveAt DESC")
    suspend fun getByThreadId(threadId: String): List<StateTransitionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transition: StateTransitionEntity)

    @Query("SELECT * FROM state_transitions WHERE triggeringEventId = :eventId")
    suspend fun getByEventId(eventId: String): StateTransitionEntity?
}
