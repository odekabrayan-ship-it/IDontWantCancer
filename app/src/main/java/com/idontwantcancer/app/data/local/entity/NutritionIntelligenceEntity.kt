package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.EvidenceStrength

@Entity(tableName = "nutrition_intelligence")
data class NutritionIntelligenceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val evidenceLevel: EvidenceStrength,
    val theTruth: String,
    val theCommand: String,
    val theExecutionJson: String, // Serialized list of String
    val theShield: String,
    val source: String,
    val sourceUrl: String?
)
