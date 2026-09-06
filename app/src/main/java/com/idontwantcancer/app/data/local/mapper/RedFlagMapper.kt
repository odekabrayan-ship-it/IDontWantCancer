package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.RedFlagDirectiveEntity
import com.idontwantcancer.app.domain.model.RedFlagDirective
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun RedFlagDirectiveEntity.toDomain(): RedFlagDirective {
    return RedFlagDirective(
        id = id,
        title = title,
        summary = summary,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecution = try {
            Json.decodeFromString(theExecutionJson)
        } catch (e: Exception) {
            emptyList()
        },
        whileYouWait = try {
            Json.decodeFromString(whileYouWaitJson)
        } catch (e: Exception) {
            emptyList()
        },
        handoffScript = handoffScript,
        theShield = theShield
    )
}

fun RedFlagDirective.toEntity(): RedFlagDirectiveEntity {
    return RedFlagDirectiveEntity(
        id = id,
        title = title,
        summary = summary,
        theTruth = theTruth,
        theCommand = theCommand,
        theExecutionJson = Json.encodeToString(theExecution),
        whileYouWaitJson = Json.encodeToString(whileYouWait),
        handoffScript = handoffScript,
        theShield = theShield
    )
}
