package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.SourceMaterialEntity
import com.idontwantcancer.app.domain.model.SourceMaterial
import java.time.Instant

/**
 * Maps between [SourceMaterial] domain model and [SourceMaterialEntity] persistent model.
 */

fun SourceMaterialEntity.toDomain(): SourceMaterial {
    return SourceMaterial(
        sourceId = sourceId,
        contentId = contentId,
        title = title,
        content = content,
        publishedAt = Instant.ofEpochMilli(publishedAt),
        updatedAt = updatedAt?.let { Instant.ofEpochMilli(it) },
        canonicalUrl = canonicalUrl,
        contentHash = contentHash
    )
}

fun SourceMaterial.toEntity(
    firstObservedAt: Long = System.currentTimeMillis()
): SourceMaterialEntity {
    return SourceMaterialEntity(
        contentId = contentId,
        sourceId = sourceId,
        title = title,
        content = content,
        publishedAt = publishedAt.toEpochMilli(),
        updatedAt = updatedAt?.toEpochMilli(),
        canonicalUrl = canonicalUrl,
        contentHash = contentHash,
        firstObservedAt = firstObservedAt,
        lastProcessedAt = System.currentTimeMillis()
    )
}
