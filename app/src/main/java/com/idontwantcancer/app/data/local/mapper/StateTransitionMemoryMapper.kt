package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.StateTransitionEntity
import com.idontwantcancer.app.domain.model.IntelligenceStateTransition
import java.time.Instant

/**
 * Maps between [IntelligenceStateTransition] domain model and [StateTransitionEntity] persistent model.
 */

fun StateTransitionEntity.toDomain(): IntelligenceStateTransition {
    return IntelligenceStateTransition(
        id = id,
        threadId = threadId,
        previousStateEntryId = previousStateEntryId,
        resultingStateEntryId = resultingStateEntryId,
        type = type,
        triggeringEventId = triggeringEventId,
        effectiveAt = Instant.ofEpochMilli(effectiveAt),
        reason = reason
    )
}

fun IntelligenceStateTransition.toEntity(): StateTransitionEntity {
    return StateTransitionEntity(
        id = id,
        threadId = threadId,
        previousStateEntryId = previousStateEntryId,
        resultingStateEntryId = resultingStateEntryId,
        type = type,
        triggeringEventId = triggeringEventId,
        effectiveAt = effectiveAt.toEpochMilli(),
        reason = reason
    )
}
