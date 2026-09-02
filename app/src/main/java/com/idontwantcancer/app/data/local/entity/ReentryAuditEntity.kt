package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import com.idontwantcancer.app.domain.model.ReentryLifecycleState

/**
 * Persistent representation of a re-entry audit record.
 */
@Entity(
    tableName = "reentry_audit",
    primaryKeys = ["reentryIdentity", "sequenceNumber"]
)
data class ReentryAuditEntity(
    val reentryIdentity: String,
    val sequenceNumber: Int,
    val previousState: ReentryLifecycleState?,
    val newState: ReentryLifecycleState,
    val transitionReason: String?,
    val timestamp: Long
)
