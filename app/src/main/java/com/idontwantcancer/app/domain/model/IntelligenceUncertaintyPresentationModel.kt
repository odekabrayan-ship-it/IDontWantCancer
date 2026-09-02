package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * A structured representation of the uncertainty dimensions that must be 
 * preserved when communicating an intelligence item. 
 * This model ensures that the presentation layer can accurately reflect the 
 * agency's confidence, evidence gaps, and conflicts.
 */
@Serializable
data class IntelligenceUncertaintyPresentationModel(
    val intelligenceId: String,
    
    // Core uncertainty dimensions (structural references)
    val synthesisLevel: EvidenceSynthesisLevel,
    val confidenceLevel: SignalConfidence,
    val gapLevel: EvidenceGapLevel,
    val resolutionStatus: ResolutionStatus,
    
    // Presentation-guiding flags
    val requiresQualification: Boolean,
    val isContested: Boolean,
    val isDeveloping: Boolean,
    val isIncomplete: Boolean
)
