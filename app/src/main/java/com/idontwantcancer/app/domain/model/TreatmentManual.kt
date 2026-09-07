package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Categorizes treatment manuals by medical modality or recovery phase.
 */
enum class TreatmentCategory {
    CHEMO,
    RADIATION,
    SURGERY,
    DAILY_HYGIENE,
    RECOVERY
}

/**
 * Represents a specialized protective directive for cancer treatment support.
 * Follows the 4-Point Directive Protocol: Truth, Command, Execution, Shield.
 */
@Serializable
data class TreatmentManual(
    val id: String,
    val title: String,
    val summary: String,
    val category: TreatmentCategory,
    val theTruth: String,
    val theCommand: String,
    val theExecution: List<String>,
    val theShield: String
)
