package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceBriefingContinuityEngine] that maps
 * change awareness results and domain states into a structured continuity result.
 */
class DefaultIntelligenceBriefingContinuityEngine @Inject constructor() : IntelligenceBriefingContinuityEngine {

    override fun determineContinuity(
        currentBriefingId: String,
        previousBriefingId: String?,
        itemChange: IntelligenceBriefingItemChange,
        pkg: IntelligenceCommunicationPackage
    ): IntelligenceBriefingContinuityResult {
        
        val continuityState = when (itemChange.level) {
            BriefingItemChangeLevel.NEW -> BriefingContinuityState.NEW
            BriefingItemChangeLevel.UNCHANGED -> BriefingContinuityState.CONTINUING
            BriefingItemChangeLevel.UPDATED -> BriefingContinuityState.UPDATED
            BriefingItemChangeLevel.MATERIALLY_CHANGED -> {
                if (pkg.continuity?.level == ContinuityLevel.REVERSAL) BriefingContinuityState.REVERSAL
                else BriefingContinuityState.MATERIAL_CHANGE
            }
            BriefingItemChangeLevel.REACTIVATED -> BriefingContinuityState.REACTIVATED
            BriefingItemChangeLevel.SUPERSEDED -> BriefingContinuityState.SUPERSEDED
            BriefingItemChangeLevel.REMOVED_FROM_BRIEFING -> BriefingContinuityState.HISTORICAL
        }

        return IntelligenceBriefingContinuityResult(
            currentBriefingId = currentBriefingId,
            previousBriefingId = previousBriefingId,
            itemId = "item-${pkg.intelligenceId}",
            intelligenceId = pkg.intelligenceId,
            threadId = pkg.threadId,
            continuityState = continuityState,
            previousIntelligenceReference = itemChange.signalId, // Assuming ID is reference
            currentIntelligenceReference = pkg.intelligenceId,
            stateTransitionReference = pkg.currentState.effectiveEntryId,
            narrativeSequenceReference = pkg.threadId,
            supersessionReference = pkg.currentState.lastMeaningfulChangeId,
            briefingChangeReference = itemChange.level.name,
            conflictReference = if (pkg.conflicts.isNotEmpty()) pkg.conflicts.first().id else null,
            evidenceStateReference = pkg.evidenceSynthesis.level.name,
            provenanceReference = pkg.provenance.signalId
        )
    }
}
