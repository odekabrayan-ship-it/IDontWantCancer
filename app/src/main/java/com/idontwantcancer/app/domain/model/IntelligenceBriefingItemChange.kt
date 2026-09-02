package com.idontwantcancer.app.domain.model

/**
 * Represents the structured comparison result for a single briefing item.
 */
data class IntelligenceBriefingItemChange(
    val signalId: String,
    val level: BriefingItemChangeLevel,
    val previousPosition: Int?,
    val currentPosition: Int?,
    val reason: String,
    
    // Pointers to domain context supporting the change
    val continuityLevel: ContinuityLevel? = null,
    val stateTransitionId: String? = null
)
