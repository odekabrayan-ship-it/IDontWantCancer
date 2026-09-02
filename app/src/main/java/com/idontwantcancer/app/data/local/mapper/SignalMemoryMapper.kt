package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.SignalEntity
import com.idontwantcancer.app.domain.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant

/**
 * Maps between [Signal] domain model and [SignalEntity] persistent model.
 */

fun SignalEntity.toDomain(): Signal {
    return Signal(
        id = id,
        title = title,
        summary = summary,
        significance = significance,
        significanceLevel = significanceLevel,
        significanceFactors = try {
            Json.decodeFromString(significanceFactorsJson)
        } catch (e: Exception) {
            emptyList()
        },
        explanation = explanation,
        category = category,
        importance = importance,
        confidence = confidence,
        confidenceFactors = try {
            Json.decodeFromString(confidenceFactorsJson)
        } catch (e: Exception) {
            emptyList()
        },
        conflictStatus = conflictStatus,
        detectedAt = Instant.ofEpochMilli(detectedAt),
        publishedAt = Instant.ofEpochMilli(publishedAt),
        recommendedAction = recommendedAction,
        source = SignalSource(
            name = sourceName,
            url = sourceUrl
        ),
        supportingSources = try {
            Json.decodeFromString(supportingSourcesJson)
        } catch (e: Exception) {
            emptyList()
        },
        lastAdmittedStateEntryId = lastAdmittedStateEntryId
    )
}

fun Signal.toEntity(
    lifecycle: IntelligenceLifecycle = IntelligenceLifecycle.DETECTED,
    isBriefed: Boolean = false,
    firstObservedAt: Long = System.currentTimeMillis()
): SignalEntity {
    return SignalEntity(
        id = id,
        title = title,
        summary = summary,
        significance = significance,
        significanceLevel = significanceLevel,
        significanceFactorsJson = Json.encodeToString(significanceFactors),
        explanation = explanation,
        category = category,
        importance = importance,
        confidence = confidence,
        confidenceFactorsJson = Json.encodeToString(confidenceFactors),
        conflictStatus = conflictStatus,
        detectedAt = detectedAt.toEpochMilli(),
        publishedAt = publishedAt.toEpochMilli(),
        recommendedAction = recommendedAction,
        sourceName = source.name,
        sourceUrl = source.url,
        supportingSourcesJson = Json.encodeToString(supportingSources),
        lifecycle = lifecycle,
        isBriefed = isBriefed,
        firstObservedAt = firstObservedAt,
        lastUpdatedAt = System.currentTimeMillis(),
        lastAdmittedStateEntryId = lastAdmittedStateEntryId
    )
}
