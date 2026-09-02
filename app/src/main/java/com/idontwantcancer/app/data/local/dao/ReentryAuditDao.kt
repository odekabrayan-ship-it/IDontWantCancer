package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.ReentryAuditEntity

@Dao
interface ReentryAuditDao {
    /**
     * Appends a new audit record. Fails if the sequence number already exists 
     * for the given identity, enforcing append-only immutability.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entry: ReentryAuditEntity)

    @Query("SELECT * FROM reentry_audit WHERE reentryIdentity = :identity ORDER BY sequenceNumber ASC")
    suspend fun getByReentryIdentity(identity: String): List<ReentryAuditEntity>
}
