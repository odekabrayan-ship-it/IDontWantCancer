package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * A Signal represents a meaningful change detected by the cancer intelligence system.
 */
@Serializable
data class Signal(
    val id: String,
    val title: String,
    val summary: String,
    val significance: String? = null,
    val significanceLevel: SignificanceOutcome? = null,
    val significanceFactors: List<String> = emptyList(),
    val explanation: String? = null,
    val category: SignalCategory,
    val importance: SignalImportance,
    val confidence: SignalConfidence,
    val confidenceFactors: List<EvidenceFactor> = emptyList(),
    val conflictStatus: ResolutionStatus? = null,
    @Serializable(with = InstantSerializer::class)
    val detectedAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val publishedAt: Instant,
    val recommendedAction: String? = null,
    val source: SignalSource,
    val supportingSources: List<SignalSource> = emptyList(),

    // Actionability (Step 222)
    val isActionable: Boolean = false,
    val actionType: ActionType = ActionType.NONE,

    // Truth Check (Step 222 - Feature 4)
    val verdict: EvidenceVerdict? = null,
    val investigatedClaim: String? = null,

    // Geographical Scope (Step 222 - V2)
    val scope: GeographicScope = GeographicScope.GLOBAL,
    val targetCountryCode: String? = null,

    // Consumer Safety (Step 222 - Feature 2)
    val affectedIngredients: List<String> = emptyList(),
    val safetyLevel: SafetyLevel = SafetyLevel.UNDEFINED,
    val safeAlternatives: List<String> = emptyList(),

    // Directive Protocol (Step 222 - Overhaul Stage 1)
    val theTruth: String? = null,
    val theCommand: String? = null,
    val theExecution: List<String> = emptyList(),
    val theShield: String? = null,
    val isActionTaken: Boolean = false,
    val isWatched: Boolean = false,
    
    // Admission Context
    val lastAdmittedStateEntryId: String? = null
)
