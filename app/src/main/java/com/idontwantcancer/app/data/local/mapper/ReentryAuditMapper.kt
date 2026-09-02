package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.ReentryAuditEntity
import com.idontwantcancer.app.domain.model.IntelligenceReentryAuditEntry
import java.time.Instant

/**
 * Maps between [IntelligenceReentryAuditEntry] domain model and [ReentryAuditEntity].
 */

fun ReentryAuditEntity.toDomain(): IntelligenceReentryAuditEntry {
    return IntelligenceReentryAuditEntry(
        reentryIdentity = reentryIdentity,
        sequenceNumber = sequenceNumber,
        previousState = previousState,
        newState = newState,
        transitionReason = transitionReason,
        timestamp = Instant.ofEpochMilli(timestamp)
    )
}

fun IntelligenceReentryAuditEntry.toEntity(): ReentryAuditEntity {
    return ReentryAuditEntity(
        reentryIdentity = reentryIdentity,
        sequenceNumber = sequenceNumber,
        previousState = previousState,
        newState = newState,
        transitionReason = transitionReason,
        timestamp = timestamp.toEpochMilli()
    )
}
