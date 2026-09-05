package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.TreatmentManualEntity
import com.idontwantcancer.app.domain.model.TreatmentManual
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun TreatmentManualEntity.toDomain(): TreatmentManual {
    return TreatmentManual(
        id = id,
        title = title,
        summary = summary,
        category = category,
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

fun TreatmentManual.toEntity(): TreatmentManualEntity {
    return TreatmentManualEntity(
        id = id,
        title = title,
        summary = summary,
        category = category,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecutionJson = Json.encodeToString(theExecution),
        theShield = theShield
    )
}
