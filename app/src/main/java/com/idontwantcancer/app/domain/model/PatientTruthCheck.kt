package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Categorizes the verdict of a patient-specific health claim.
 */
enum class PatientVerdict {
    SCAM,
    UNCERTAIN,
    COMPLEMENTARY
}

/**
 * Represents a specialized truth-check directive to protect patients from misinformation.
 * Follows the 4-Point Directive Protocol and adds a Social Script.
 */
@Serializable
data class PatientTruthCheck(
    val id: String,
    val claim: String,
    val verdict: PatientVerdict,
    val theTruth: String,
    val theCommand: String,
    val theExecution: List<String>,
    val theShield: String,
    val socialScript: String
)
