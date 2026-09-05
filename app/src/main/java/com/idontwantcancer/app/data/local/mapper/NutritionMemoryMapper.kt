package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.NutritionIntelligenceEntity
import com.idontwantcancer.app.domain.model.NutritionIntelligence
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun NutritionIntelligenceEntity.toDomain(): NutritionIntelligence {
    return NutritionIntelligence(
        id = id,
        title = title,
        summary = summary,
        evidenceLevel = evidenceLevel,
        category = category,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecution = try {
            Json.decodeFromString(theExecutionJson)
        } catch (e: Exception) {
            emptyList()
        },
        theShield = theShield,
        source = source,
        sourceUrl = sourceUrl
    )
}

fun NutritionIntelligence.toEntity(): NutritionIntelligenceEntity {
    return NutritionIntelligenceEntity(
        id = id,
        title = title,
        summary = summary,
        evidenceLevel = evidenceLevel,
        category = category,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecutionJson = Json.encodeToString(theExecution),
        theShield = theShield,
        source = source,
        sourceUrl = sourceUrl
    )
}
