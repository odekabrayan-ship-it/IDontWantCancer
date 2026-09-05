package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.HealingLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealingLogDao {
    @Query("SELECT * FROM healing_log ORDER BY timestamp DESC")
    fun getAllFlow(): Flow<List<HealingLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HealingLogEntity)

    @Query("DELETE FROM healing_log")
    suspend fun deleteAll()
}
