package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.TimelineEntryType

/**
 * Persistent representation of a single timeline event.
 */
@Entity(
    tableName = "timeline_entries",
    indices = [Index(value = ["threadId"])]
)
data class TimelineEntryEntity(
    @PrimaryKey val id: String,
    val threadId: String,
    val type: TimelineEntryType,
    val description: String,
    
    // Temporal context (Epoch millis)
    val eventTime: Long?,
    val sourceTime: Long?,
    val publicationTime: Long?,
    val ingestionTime: Long,
    
    // Provenance
    val sourceMaterialId: String?,
    val changeId: String?,
    val signalId: String?,
    
    val stateSnapshot: String?
)
