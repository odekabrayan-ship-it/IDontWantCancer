package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persistent representation of raw information obtained from an authoritative source.
 */
@Entity(tableName = "source_materials")
data class SourceMaterialEntity(
    @PrimaryKey val contentId: String,
    val sourceId: String,
    val title: String,
    val content: String,
    val publishedAt: Long, // Epoch millis
    val updatedAt: Long?,
    val canonicalUrl: String?,
    val contentHash: String,
    val firstObservedAt: Long,
    val lastProcessedAt: Long
)
