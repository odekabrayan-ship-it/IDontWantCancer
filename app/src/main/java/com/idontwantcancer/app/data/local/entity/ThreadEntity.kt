package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persistent representation of an intelligence thread.
 */
@Entity(tableName = "threads")
data class ThreadEntity(
    @PrimaryKey val id: String,
    val topicIdentifier: String,
    val firstDetectedAt: Long,
    val lastUpdatedAt: Long,
    val currentStatus: String?,
    val signalIds: List<String>
)
