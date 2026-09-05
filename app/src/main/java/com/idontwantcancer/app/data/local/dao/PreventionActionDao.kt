package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.PreventionActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreventionActionDao {
    @Query("SELECT * FROM prevention_actions")
    fun getAllFlow(): Flow<List<PreventionActionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<PreventionActionEntity>)

    @Query("UPDATE prevention_actions SET isAdopted = :isAdopted WHERE id = :id")
    suspend fun updateAdoptionStatus(id: String, isAdopted: Boolean)

    @Query("SELECT * FROM prevention_actions WHERE id = :id")
    suspend fun getById(id: String): PreventionActionEntity?
}
