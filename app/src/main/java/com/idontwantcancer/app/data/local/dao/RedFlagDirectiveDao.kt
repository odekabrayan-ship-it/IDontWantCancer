package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.RedFlagDirectiveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RedFlagDirectiveDao {
    @Query("SELECT * FROM red_flag_directives")
    fun getAllFlow(): Flow<List<RedFlagDirectiveEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<RedFlagDirectiveEntity>)

    @Query("DELETE FROM red_flag_directives")
    suspend fun deleteAll()
}
