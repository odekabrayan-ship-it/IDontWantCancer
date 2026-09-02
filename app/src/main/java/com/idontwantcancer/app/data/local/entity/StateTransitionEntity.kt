package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.IntelligenceStateTransitionType

/**
 * Persistent representation of an intelligence state transition.
 */
@Entity(
    tableName = "state_transitions",
    indices = [
        Index(value = ["threadId"]),
        Index(value = ["triggeringEventId"])
    ]
)
data class StateTransitionEntity(
    @PrimaryKey val id: String,
    val threadId: String,
    val previousStateEntryId: String?,
    val resultingStateEntryId: String,
    val type: IntelligenceStateTransitionType,
    val triggeringEventId: String,
    val effectiveAt: Long,
    val reason: String
)
