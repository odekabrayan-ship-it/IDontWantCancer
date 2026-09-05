package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.TreatmentCategory

@Entity(tableName = "treatment_manuals")
data class TreatmentManualEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val category: TreatmentCategory,
    val theTruth: String,
    val theCommand: String,
    val theExecutionJson: String, // Serialized list of String
    val theShield: String
)
