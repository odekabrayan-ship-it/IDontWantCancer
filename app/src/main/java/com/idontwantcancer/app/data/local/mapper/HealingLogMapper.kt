package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.HealingLogEntity
import com.idontwantcancer.app.domain.model.HealingLogEntry
import java.time.Instant

fun HealingLogEntity.toDomain(): HealingLogEntry {
    return HealingLogEntry(
        id = id,
        directiveId = directiveId,
        directiveName = directiveName,
        timestamp = Instant.ofEpochMilli(timestamp),
        type = type
    )
}

fun HealingLogEntry.toEntity(): HealingLogEntity {
    return HealingLogEntity(
        id = id,
        directiveId = directiveId,
        directiveName = directiveName,
        timestamp = timestamp.toEpochMilli(),
        type = type
    )
}
