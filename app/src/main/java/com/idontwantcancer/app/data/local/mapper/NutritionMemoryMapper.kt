package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.NutritionIntelligenceEntity
import com.idontwantcancer.app.domain.model.NutritionIntelligence

fun NutritionIntelligenceEntity.toDomain(): NutritionIntelligence {
    return NutritionIntelligence(
        id = id,
        title = title,
        summary = summary,
        evidenceLevel = evidenceLevel,
        reality = reality,
        recommendation = recommendation,
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
        reality = reality,
        recommendation = recommendation,
        source = source,
        sourceUrl = sourceUrl
    )
}
