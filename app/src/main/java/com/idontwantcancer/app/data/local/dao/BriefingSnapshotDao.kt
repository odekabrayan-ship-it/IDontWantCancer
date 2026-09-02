package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.BriefingSnapshotEntity

@Dao
interface BriefingSnapshotDao {
    @Upsert
    suspend fun upsert(snapshot: BriefingSnapshotEntity)

    @Query("SELECT * FROM briefing_snapshots WHERE id = :id")
    suspend fun getById(id: String): BriefingSnapshotEntity?

    @Query("SELECT * FROM briefing_snapshots ORDER BY generatedAt DESC")
    suspend fun getAllSnapshots(): List<BriefingSnapshotEntity>

    @Query("SELECT * FROM briefing_snapshots ORDER BY generatedAt DESC LIMIT 1")
    suspend fun getLatestSnapshot(): BriefingSnapshotEntity?
}
