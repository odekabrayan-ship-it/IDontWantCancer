package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.EvidenceStrength
import com.idontwantcancer.app.domain.model.NutritionCategory

@Entity(tableName = "nutrition_intelligence")
data class NutritionIntelligenceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val evidenceLevel: EvidenceStrength,
    val category: NutritionCategory,
    val theTruth: String,
    val theCommand: String,
    val theExecutionJson: String, // Serialized list of String
    val theShield: String,
    val switchThisForThat: String?,
    val source: String,
    val sourceUrl: String?
)
