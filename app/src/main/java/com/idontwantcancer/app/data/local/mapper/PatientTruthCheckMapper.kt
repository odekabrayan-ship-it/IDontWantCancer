package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.PatientTruthCheckEntity
import com.idontwantcancer.app.domain.model.PatientTruthCheck
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun PatientTruthCheckEntity.toDomain(): PatientTruthCheck {
    return PatientTruthCheck(
        id = id,
        claim = claim,
        verdict = verdict,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecution = try {
            Json.decodeFromString(theExecutionJson)
        } catch (e: Exception) {
            emptyList()
        },
        theShield = theShield,
        socialScript = socialScript
    )
}

fun PatientTruthCheck.toEntity(): PatientTruthCheckEntity {
    return PatientTruthCheckEntity(
        id = id,
        claim = claim,
        verdict = verdict,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecutionJson = Json.encodeToString(theExecution),
        theShield = theShield,
        socialScript = socialScript
    )
}
