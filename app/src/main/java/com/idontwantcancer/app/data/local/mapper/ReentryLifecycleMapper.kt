package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.ReentryLifecycleEntity
import com.idontwantcancer.app.domain.model.IntelligenceReentryLifecycle
import java.time.Instant

/**
 * Maps between [IntelligenceReentryLifecycle] domain model and [ReentryLifecycleEntity].
 */

fun ReentryLifecycleEntity.toDomain(): IntelligenceReentryLifecycle {
    return IntelligenceReentryLifecycle(
        reentryIdentity = reentryIdentity,
        currentState = currentState,
        intelligenceId = intelligenceId,
        stateEntryId = stateEntryId,
        admittedAt = Instant.ofEpochMilli(admittedAt),
        lastTransitionAt = Instant.ofEpochMilli(lastTransitionAt),
        transitionReason = transitionReason
    )
}

fun IntelligenceReentryLifecycle.toEntity(): ReentryLifecycleEntity {
    return ReentryLifecycleEntity(
        reentryIdentity = reentryIdentity,
        currentState = currentState,
        intelligenceId = intelligenceId,
        stateEntryId = stateEntryId,
        admittedAt = admittedAt.toEpochMilli(),
        lastTransitionAt = lastTransitionAt.toEpochMilli(),
        transitionReason = transitionReason
    )
}
