package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the structured differences for a single item in a communication cycle.
 */
@Serializable
data class BriefingItemReconciliationDiff(
    val intelligenceId: String,
    val isSuperseded: Boolean,
    val stateChanged: Boolean,
    val significanceChanged: Boolean,
    val priorityChanged: Boolean,
    val evidenceChanged: Boolean,
    val conflictChanged: Boolean,
    
    // Previous (Snapshot) vs Current values
    val previousStateEntryId: String,
    val currentStateEntryId: String?,
    
    val previousSignificance: SignificanceOutcome,
    val currentSignificance: SignificanceOutcome?,
    
    val previousPriority: AttentionLevel,
    val currentPriority: AttentionLevel?,
    
    val previousEvidenceLevel: EvidenceSynthesisLevel,
    val currentEvidenceLevel: EvidenceSynthesisLevel?
)
