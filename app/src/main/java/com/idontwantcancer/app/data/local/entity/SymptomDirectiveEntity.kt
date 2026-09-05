package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "symptom_directives")
data class SymptomDirectiveEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconName: String,
    val theTruth: String,
    val theCommand: String,
    val theExecutionJson: String, // Serialized list of String
    val theShield: String
)
