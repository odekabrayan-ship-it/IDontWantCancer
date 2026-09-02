package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.SupersessionRelationEntity
import com.idontwantcancer.app.domain.model.SupersessionRelation
import java.time.Instant

/**
 * Maps between [SupersessionRelation] domain model and [SupersessionRelationEntity] persistent model.
 */

fun SupersessionRelationEntity.toDomain(): SupersessionRelation {
    return SupersessionRelation(
        id = id,
        previousEntryId = previousEntryId,
        supersedingEntryId = supersedingEntryId,
        type = type,
        reason = reason,
        detectedAt = Instant.ofEpochMilli(detectedAt)
    )
}

fun SupersessionRelation.toEntity(): SupersessionRelationEntity {
    return SupersessionRelationEntity(
        id = id,
        previousEntryId = previousEntryId,
        supersedingEntryId = supersedingEntryId,
        type = type,
        reason = reason,
        detectedAt = detectedAt.toEpochMilli()
    )
}
