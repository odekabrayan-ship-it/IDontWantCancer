package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.SymptomDirectiveEntity
import com.idontwantcancer.app.domain.model.SymptomDirective
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun SymptomDirectiveEntity.toDomain(): SymptomDirective {
    return SymptomDirective(
        id = id,
        name = name,
        iconName = iconName,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecution = try {
            Json.decodeFromString(theExecutionJson)
        } catch (e: Exception) {
            emptyList()
        },
        theShield = theShield
    )
}

fun SymptomDirective.toEntity(): SymptomDirectiveEntity {
    return SymptomDirectiveEntity(
        id = id,
        name = name,
        iconName = iconName,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecutionJson = Json.encodeToString(theExecution),
        theShield = theShield
    )
}
