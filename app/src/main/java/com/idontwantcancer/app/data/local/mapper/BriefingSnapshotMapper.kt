package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.BriefingSnapshotEntity
import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Maps between [IntelligenceBriefing] and [BriefingSnapshotEntity].
 */

fun IntelligenceBriefing.toEntity(): BriefingSnapshotEntity {
    return BriefingSnapshotEntity(
        id = id,
        cycleId = cycleId,
        generatedAt = generatedAt.toEpochMilli(),
        status = status,
        briefingJson = Json.encodeToString(this)
    )
}

fun BriefingSnapshotEntity.toDomain(): IntelligenceBriefing {
    return Json.decodeFromString(briefingJson)
}
