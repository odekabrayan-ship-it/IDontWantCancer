package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.ConsolidatedEventEntity
import com.idontwantcancer.app.domain.model.ConsolidatedEvent
import java.time.Instant

/**
 * Maps between [ConsolidatedEvent] domain model and [ConsolidatedEventEntity] persistent model.
 */

fun ConsolidatedEventEntity.toDomain(): ConsolidatedEvent {
    return ConsolidatedEvent(
        id = id,
        topicIdentifier = topicIdentifier,
        sourceMaterialIds = sourceMaterialIds,
        firstDetectedAt = Instant.ofEpochMilli(firstDetectedAt),
        lastUpdatedAt = Instant.ofEpochMilli(lastUpdatedAt),
        threadId = threadId
    )
}

fun ConsolidatedEvent.toEntity(): ConsolidatedEventEntity {
    return ConsolidatedEventEntity(
        id = id,
        topicIdentifier = topicIdentifier,
        sourceMaterialIds = sourceMaterialIds,
        firstDetectedAt = firstDetectedAt.toEpochMilli(),
        lastUpdatedAt = lastUpdatedAt.toEpochMilli(),
        threadId = threadId
    )
}
