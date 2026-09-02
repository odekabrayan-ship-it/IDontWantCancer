package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.BriefingStatus

/**
 * Persistent representation of a historical intelligence briefing snapshot.
 * Preserves the exact state of communication at a specific point in time.
 */
@Entity(tableName = "briefing_snapshots")
data class BriefingSnapshotEntity(
    @PrimaryKey val id: String,
    val cycleId: String,
    val generatedAt: Long,
    val status: BriefingStatus,
    val briefingJson: String // Serialized IntelligenceBriefing
)
