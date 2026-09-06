package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "red_flag_directives")
data class RedFlagDirectiveEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val theTruth: String,
    val theCommand: String,
    val theExecutionJson: String,
    val whileYouWaitJson: String,
    val handoffScript: String,
    val theShield: String
)
