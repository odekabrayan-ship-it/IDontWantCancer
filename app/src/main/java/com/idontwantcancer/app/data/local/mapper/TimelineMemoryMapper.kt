package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.TimelineEntryEntity
import com.idontwantcancer.app.domain.model.TimelineEntry
import java.time.Instant

/**
 * Maps between [TimelineEntry] domain model and [TimelineEntryEntity] persistent model.
 */

fun TimelineEntryEntity.toDomain(): TimelineEntry {
    return TimelineEntry(
        id = id,
        threadId = threadId,
        type = type,
        description = description,
        eventTime = eventTime?.let { Instant.ofEpochMilli(it) },
        sourceTime = sourceTime?.let { Instant.ofEpochMilli(it) },
        publicationTime = publicationTime?.let { Instant.ofEpochMilli(it) },
        ingestionTime = Instant.ofEpochMilli(ingestionTime),
        sourceMaterialId = sourceMaterialId,
        changeId = changeId,
        signalId = signalId,
        stateSnapshot = stateSnapshot
    )
}

fun TimelineEntry.toEntity(): TimelineEntryEntity {
    return TimelineEntryEntity(
        id = id,
        threadId = threadId,
        type = type,
        description = description,
        eventTime = eventTime?.toEpochMilli(),
        sourceTime = sourceTime?.toEpochMilli(),
        publicationTime = publicationTime?.toEpochMilli(),
        ingestionTime = ingestionTime.toEpochMilli(),
        sourceMaterialId = sourceMaterialId,
        changeId = changeId,
        signalId = signalId,
        stateSnapshot = stateSnapshot
    )
}
