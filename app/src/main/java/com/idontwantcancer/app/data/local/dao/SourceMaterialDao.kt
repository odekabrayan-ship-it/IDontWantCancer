package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.SourceMaterialEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [SourceMaterialEntity].
 */
@Dao
interface SourceMaterialDao {
    @Query("SELECT * FROM source_materials WHERE contentId = :contentId")
    suspend fun getById(contentId: String): SourceMaterialEntity?

    @Query("SELECT * FROM source_materials WHERE contentHash = :hash")
    suspend fun getByHash(hash: String): SourceMaterialEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(material: SourceMaterialEntity)

    @Query("SELECT * FROM source_materials ORDER BY lastProcessedAt DESC")
    fun getAllFlow(): Flow<List<SourceMaterialEntity>>
}
