package com.idontwantcancer.app.domain.model

/**
 * Represents the structured continuity relationship for a briefing item.
 */
data class IntelligenceBriefingContinuityResult(
    val currentBriefingId: String,
    val previousBriefingId: String?,
    val itemId: String,
    val intelligenceId: String,
    val threadId: String,
    val continuityState: BriefingContinuityState,
    
    // References to related lifecycle nodes
    val previousIntelligenceReference: String?,
    val currentIntelligenceReference: String,
    val stateTransitionReference: String?,
    val narrativeSequenceReference: String?,
    val supersessionReference: String?,
    val briefingChangeReference: String?,
    val conflictReference: String?,
    val evidenceStateReference: String?,
    val provenanceReference: String
)
