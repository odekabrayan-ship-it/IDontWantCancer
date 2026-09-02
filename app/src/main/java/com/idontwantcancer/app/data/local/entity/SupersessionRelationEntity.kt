package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.SupersessionType

/**
 * Persistent representation of a supersession relationship between intelligence entries.
 */
@Entity(
    tableName = "supersession_relations",
    indices = [
        Index(value = ["previousEntryId"]),
        Index(value = ["supersedingEntryId"])
    ]
)
data class SupersessionRelationEntity(
    @PrimaryKey val id: String,
    val previousEntryId: String,
    val supersedingEntryId: String,
    val type: SupersessionType,
    val reason: String,
    val detectedAt: Long // Epoch millis
)
