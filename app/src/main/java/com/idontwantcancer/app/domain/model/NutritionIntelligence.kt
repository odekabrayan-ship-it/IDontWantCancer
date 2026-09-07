package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a permanent, evidence-based nutritional truth for cancer prevention.
 * Every item follows the 4-Point Directive Protocol: Truth, Command, Execution, Shield.
 */
@Serializable
data class NutritionIntelligence(
    val id: String,
    val title: String,
    val summary: String,
    val evidenceLevel: EvidenceStrength,
    val category: NutritionCategory,
    val theTruth: String,
    val theCommand: String,
    val theExecution: List<String>,
    val theShield: String,
    val switchThisForThat: String? = null,
    val source: String,
    val sourceUrl: String? = null
)
