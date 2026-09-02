package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.ConflictType
import com.idontwantcancer.app.domain.model.ResolutionStatus

/**
 * Persistent representation of an identified disagreement between intelligence sources.
 */
@Entity(tableName = "conflicts")
data class ConflictEntity(
    @PrimaryKey val id: String,
    val topicIdentifier: String,
    val participatingSourceIds: List<String>,
    val type: ConflictType,
    val resolutionStatus: ResolutionStatus,
    val competingSignalIds: List<String>,
    val detectedAt: Long, // Epoch millis
    val resolvedAt: Long?,
    val resolutionReasoning: String?
)
