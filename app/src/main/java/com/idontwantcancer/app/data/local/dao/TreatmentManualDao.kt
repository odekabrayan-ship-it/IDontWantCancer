package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.TreatmentManualEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TreatmentManualDao {
    @Query("SELECT * FROM treatment_manuals")
    fun getAllFlow(): Flow<List<TreatmentManualEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<TreatmentManualEntity>)

    @Query("DELETE FROM treatment_manuals")
    suspend fun deleteAll()
}
