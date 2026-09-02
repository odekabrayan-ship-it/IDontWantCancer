package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the minimum structured context required to accurately understand 
 * an intelligence briefing item.
 */
@Serializable
data class IntelligenceBriefingContextResult(
    val intelligenceId: String,
    
    // References to establish context without full history
    val previousStateEntryId: String? = null,
    val relevantStateTransitionIds: List<String> = emptyList(),
    val relevantSupersessionRelationIds: List<String> = emptyList(),
    val relevantConflictIds: List<String> = emptyList(),
    val relevantEvidenceGapIds: List<String> = emptyList(),
    
    // Pointers to specifically relevant historical signals in the thread
    val relevantHistoricalSignalIds: List<String> = emptyList(),
    
    val reason: String
)
