package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.PatientTruthCheckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientTruthCheckDao {
    @Query("SELECT * FROM patient_truth_checks")
    fun getAllFlow(): Flow<List<PatientTruthCheckEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<PatientTruthCheckEntity>)

    @Query("DELETE FROM patient_truth_checks")
    suspend fun deleteAll()
}
