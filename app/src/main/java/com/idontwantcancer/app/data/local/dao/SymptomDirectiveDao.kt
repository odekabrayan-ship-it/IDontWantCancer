package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.SymptomDirectiveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SymptomDirectiveDao {
    @Query("SELECT * FROM symptom_directives")
    fun getAllFlow(): Flow<List<SymptomDirectiveEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<SymptomDirectiveEntity>)

    @Query("DELETE FROM symptom_directives")
    suspend fun deleteAll()
}
