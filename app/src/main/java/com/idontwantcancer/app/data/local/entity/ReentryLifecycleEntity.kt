package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.ReentryLifecycleState

/**
 * Persistent representation of a re-entry lifecycle record.
 */
@Entity(tableName = "reentry_lifecycle")
data class ReentryLifecycleEntity(
    @PrimaryKey val reentryIdentity: String,
    val currentState: ReentryLifecycleState,
    val intelligenceId: String,
    val stateEntryId: String?,
    val admittedAt: Long,
    val lastTransitionAt: Long,
    val transitionReason: String?
)
