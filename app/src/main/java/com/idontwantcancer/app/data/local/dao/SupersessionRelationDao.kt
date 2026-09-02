package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.SupersessionRelationEntity

/**
 * Data Access Object for [SupersessionRelationEntity].
 */
@Dao
interface SupersessionRelationDao {
    @Query("SELECT * FROM supersession_relations WHERE previousEntryId = :entryId")
    suspend fun getByPreviousId(entryId: String): List<SupersessionRelationEntity>

    @Query("SELECT * FROM supersession_relations WHERE supersedingEntryId = :entryId")
    suspend fun getBySupersedingId(entryId: String): List<SupersessionRelationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(relation: SupersessionRelationEntity)

    @Query("SELECT * FROM supersession_relations")
    suspend fun getAll(): List<SupersessionRelationEntity>
}
