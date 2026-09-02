package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceBriefingContextEngine] that uses 
 * structural rules to identify the minimum context required for comprehension.
 */
class DefaultIntelligenceBriefingContextEngine @Inject constructor() : IntelligenceBriefingContextEngine {

    override fun determineContext(
        pkg: IntelligenceCommunicationPackage
    ): IntelligenceBriefingContextResult {
        val relevantTransitions = mutableListOf<String>()
        val relevantHistoricalSignals = mutableListOf<String>()
        
        // 1. Mandatory Context for Changes
        // If it's a material change or reversal, the immediately preceding state is essential.
        val needsPriorState = pkg.continuity?.level == ContinuityLevel.MATERIAL_CHANGE || 
                             pkg.continuity?.level == ContinuityLevel.REVERSAL
        
        val previousStateId = if (needsPriorState) pkg.continuity?.previousStateEntryId else null

        // 2. Conflict Context
        val relevantConflicts = pkg.conflicts
            .filter { it.resolutionStatus == ResolutionStatus.UNRESOLVED }
            .map { it.id }

        // 3. Narrative Milestone context
        // We include the transition ID that established the current state.
        val narrativeEvents = pkg.narrative.sortedEvents
        val currentNarrativeEvent = narrativeEvents.find { it.timelineEntryId == pkg.currentState.effectiveEntryId }
        currentNarrativeEvent?.transitionId?.let { relevantTransitions.add(it) }

        // 4. Evidence Gap context
        val relevantGaps = if (pkg.evidenceGap.level != EvidenceGapLevel.NO_IDENTIFIED_GAP) {
            listOf(pkg.evidenceGap.threadId) // Using threadId as proxy for Gap ID for now
        } else emptyList()

        return IntelligenceBriefingContextResult(
            intelligenceId = pkg.intelligenceId,
            previousStateEntryId = previousStateId,
            relevantStateTransitionIds = relevantTransitions.distinct(),
            relevantConflictIds = relevantConflicts,
            relevantEvidenceGapIds = relevantGaps,
            relevantHistoricalSignalIds = relevantHistoricalSignals.distinct(),
            reason = if (needsPriorState) "Prior state context required for material change interpretation." 
                     else "Standard intelligence context."
        )
    }
}
