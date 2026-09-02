package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.ThreadEntity
import com.idontwantcancer.app.domain.model.IntelligenceThread
import java.time.Instant

/**
 * Maps between [IntelligenceThread] domain model and [ThreadEntity] persistent model.
 */

fun ThreadEntity.toDomain(): IntelligenceThread {
    return IntelligenceThread(
        id = id,
        topicIdentifier = topicIdentifier,
        firstDetectedAt = Instant.ofEpochMilli(firstDetectedAt),
        lastUpdatedAt = Instant.ofEpochMilli(lastUpdatedAt),
        currentStatus = currentStatus,
        signalIds = signalIds
    )
}

fun IntelligenceThread.toEntity(): ThreadEntity {
    return ThreadEntity(
        id = id,
        topicIdentifier = topicIdentifier,
        firstDetectedAt = firstDetectedAt.toEpochMilli(),
        lastUpdatedAt = lastUpdatedAt.toEpochMilli(),
        currentStatus = currentStatus,
        signalIds = signalIds
    )
}
