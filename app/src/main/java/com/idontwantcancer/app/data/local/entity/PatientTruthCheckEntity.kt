package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.PatientVerdict

@Entity(tableName = "patient_truth_checks")
data class PatientTruthCheckEntity(
    @PrimaryKey val id: String,
    val claim: String,
    val verdict: PatientVerdict,
    val theTruth: String,
    val theCommand: String,
    val theExecutionJson: String, // Serialized list of String
    val theShield: String,
    val socialScript: String
)
