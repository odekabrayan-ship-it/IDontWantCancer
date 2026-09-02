package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a single structured item within an intelligence briefing.
 * Preserves the hierarchy and context of the intelligence without altering its meaning.
 */
@Serializable
data class IntelligenceBriefingItem(
    val id: String,
    val position: Int,
    val communicationPackageId: String,
    val intelligenceId: String,
    val threadId: String,
    
    // References to establish the intelligence hierarchy
    val currentStateReference: String,
    val changeReference: String?,
    val continuityReference: ContinuityLevel?,
    val significanceReference: SignificanceOutcome,
    val priorityReference: AttentionLevel,
    val evidenceReference: EvidenceSynthesisLevel,
    val uncertaintyReference: SignalConfidence,
    val conflictReference: Boolean,
    val narrativeReference: String,
    val provenanceReference: String,
    
    // Minimum necessary context for comprehension
    val context: IntelligenceBriefingContextResult? = null,
    
    // Structured uncertainty presentation context
    val uncertaintyModel: IntelligenceUncertaintyPresentationModel? = null,
    
    // Verified re-entry lifecycle context if applicable
    val reentryContract: IntelligenceReentryHandoffConsumptionContract? = null,
    
    // Verified application-state reconciliation context if applicable
    val reconciliationContract: IntelligenceReentryReconciliationConsumptionContract? = null,
    
    val inclusionReason: String,
    val explanation: BriefingExplanation? = null,
    val readiness: CommunicationReadinessLevel
)
