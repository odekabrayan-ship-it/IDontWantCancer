package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a permanent, evidence-based nutritional truth for cancer prevention.
 */
@Serializable
data class NutritionIntelligence(
    val id: String,
    val title: String,
    val summary: String,
    val evidenceLevel: EvidenceStrength,
    val reality: String,
    val recommendation: String,
    val source: String,
    val sourceUrl: String? = null
)
