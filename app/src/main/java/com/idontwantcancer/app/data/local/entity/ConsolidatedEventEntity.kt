package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persistent representation of a consolidated intelligence event.
 */
@Entity(tableName = "consolidated_events")
data class ConsolidatedEventEntity(
    @PrimaryKey val id: String,
    val topicIdentifier: String,
    val sourceMaterialIds: List<String>,
    val firstDetectedAt: Long,
    val lastUpdatedAt: Long,
    val threadId: String?
)
