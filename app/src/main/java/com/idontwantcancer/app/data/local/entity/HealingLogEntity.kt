package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.HealingLogType

@Entity(tableName = "healing_log")
data class HealingLogEntity(
    @PrimaryKey val id: String,
    val directiveId: String,
    val directiveName: String,
    val timestamp: Long,
    val type: HealingLogType
)
