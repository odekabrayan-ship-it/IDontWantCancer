package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.ConflictEntity
import com.idontwantcancer.app.domain.model.IntelligenceConflict
import java.time.Instant

/**
 * Maps between [IntelligenceConflict] domain model and [ConflictEntity] persistent model.
 */

fun ConflictEntity.toDomain(): IntelligenceConflict {
    return IntelligenceConflict(
        id = id,
        topicIdentifier = topicIdentifier,
        participatingSourceIds = participatingSourceIds,
        type = type,
        resolutionStatus = resolutionStatus,
        competingSignalIds = competingSignalIds,
        detectedAt = Instant.ofEpochMilli(detectedAt),
        resolvedAt = resolvedAt?.let { Instant.ofEpochMilli(it) },
        resolutionReasoning = resolutionReasoning
    )
}

fun IntelligenceConflict.toEntity(): ConflictEntity {
    return ConflictEntity(
        id = id,
        topicIdentifier = topicIdentifier,
        participatingSourceIds = participatingSourceIds,
        type = type,
        resolutionStatus = resolutionStatus,
        competingSignalIds = competingSignalIds,
        detectedAt = detectedAt.toEpochMilli(),
        resolvedAt = resolvedAt?.toEpochMilli(),
        resolutionReasoning = resolutionReasoning
    )
}
