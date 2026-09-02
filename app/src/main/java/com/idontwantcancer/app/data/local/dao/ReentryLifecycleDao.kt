package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.ReentryLifecycleEntity

@Dao
interface ReentryLifecycleDao {
    @Upsert
    suspend fun upsert(entity: ReentryLifecycleEntity)

    @Query("SELECT * FROM reentry_lifecycle WHERE reentryIdentity = :identity")
    suspend fun getByIdentity(identity: String): ReentryLifecycleEntity?

    @Query("SELECT * FROM reentry_lifecycle WHERE intelligenceId = :signalId")
    suspend fun getBySignalId(signalId: String): List<ReentryLifecycleEntity>
}
