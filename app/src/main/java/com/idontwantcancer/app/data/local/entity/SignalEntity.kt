package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idontwantcancer.app.domain.model.*

/**
 * Persistent representation of an evaluated intelligence signal.
 */
@Entity(tableName = "signals")
data class SignalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val significance: String?,
    val significanceLevel: SignificanceOutcome?,
    val significanceFactorsJson: String, // Serialized list of String
    val explanation: String?,
    val category: SignalCategory,
    val importance: SignalImportance,
    val confidence: SignalConfidence,
    val confidenceFactorsJson: String, // Serialized list of EvidenceFactor
    val conflictStatus: ResolutionStatus?,
    val detectedAt: Long,
    val publishedAt: Long,
    val recommendedAction: String?,
    val sourceName: String,
    val sourceUrl: String?,
    val supportingSourcesJson: String, // Serialized list of SignalSource

    // Actionability (Step 222)
    val isActionable: Boolean = false,
    val actionType: ActionType = ActionType.NONE,

    // Truth Check (Step 222)
    val verdict: EvidenceVerdict? = null,

    // Geographical Scope (Step 222 - V2)
    val scope: GeographicScope = GeographicScope.GLOBAL,
    val targetCountryCode: String? = null,

    // Consumer Safety (Step 222 - Feature 2)
    val affectedIngredientsJson: String = "[]", // Serialized list of String
    val safetyLevel: SafetyLevel = SafetyLevel.UNDEFINED,
    
    // Memory/Lifecycle properties
    val lifecycle: IntelligenceLifecycle,
    val isBriefed: Boolean = false,
    val firstObservedAt: Long,
    val lastUpdatedAt: Long,
    
    // Re-entry/Admission context
    val lastAdmittedStateEntryId: String? = null
)
