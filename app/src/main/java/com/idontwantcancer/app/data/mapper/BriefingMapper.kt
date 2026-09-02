package com.idontwantcancer.app.data.mapper

import com.idontwantcancer.app.data.remote.model.BriefingActionDto
import com.idontwantcancer.app.data.remote.model.BriefingResponseDto
import com.idontwantcancer.app.data.remote.model.SignalDto
import com.idontwantcancer.app.domain.model.BriefingAction
import com.idontwantcancer.app.domain.model.DailyBriefing
import com.idontwantcancer.app.domain.model.DailyBriefingStatus
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.model.SignalCategory
import com.idontwantcancer.app.domain.model.SignalConfidence
import com.idontwantcancer.app.domain.model.SignalImportance
import com.idontwantcancer.app.domain.model.SignalSource
import java.time.Instant
import java.time.format.DateTimeParseException

/**
 * Maps remote DTOs to domain models for the Briefing feature.
 */

fun BriefingResponseDto.toDomain(): DailyBriefing {
    return DailyBriefing(
        generatedAt = parseInstant(generatedAt),
        status = mapToBriefingStatus(status),
        headline = headline,
        summary = summary,
        importantSignals = importantSignals.map { it.toDomain() },
        actionItems = actionItems.map { it.toDomain() }
    )
}

fun SignalDto.toDomain(): Signal {
    return Signal(
        id = id,
        title = title,
        summary = summary,
        significance = significance,
        explanation = explanation,
        category = mapToSignalCategory(category),
        importance = mapToSignalImportance(importance),
        confidence = mapToSignalConfidence(confidence),
        detectedAt = parseInstant(detectedAt),
        publishedAt = parseInstant(publishedAt),
        recommendedAction = recommendedAction,
        source = SignalSource(
            name = sourceName,
            url = sourceUrl
        )
    )
}

fun BriefingActionDto.toDomain(): BriefingAction {
    return BriefingAction(
        title = title,
        description = description
    )
}

private fun mapToBriefingStatus(status: String): DailyBriefingStatus {
    return try {
        DailyBriefingStatus.valueOf(status.uppercase())
    } catch (e: IllegalArgumentException) {
        DailyBriefingStatus.CLEAR
    }
}

private fun mapToSignalCategory(category: String): SignalCategory {
    return try {
        SignalCategory.valueOf(category.uppercase())
    } catch (e: IllegalArgumentException) {
        // Fallback to PREVENTION as a sensible default if the category is unknown
        SignalCategory.PREVENTION
    }
}

private fun mapToSignalImportance(importance: String): SignalImportance {
    return try {
        SignalImportance.valueOf(importance.uppercase())
    } catch (e: IllegalArgumentException) {
        SignalImportance.LOW
    }
}

private fun mapToSignalConfidence(confidence: String): SignalConfidence {
    return try {
        SignalConfidence.valueOf(confidence.uppercase())
    } catch (e: IllegalArgumentException) {
        SignalConfidence.LOW
    }
}

private fun parseInstant(timestamp: String): Instant {
    return try {
        Instant.parse(timestamp)
    } catch (e: DateTimeParseException) {
        // In a production app, we might want to log this or handle it differently
        // For now, we return EPOCH as a safe non-null fallback to prevent crashes
        Instant.EPOCH
    }
}
