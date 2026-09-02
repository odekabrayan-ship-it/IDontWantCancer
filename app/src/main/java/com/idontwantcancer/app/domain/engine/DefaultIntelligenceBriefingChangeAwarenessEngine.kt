package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceBriefingChangeAwarenessEngine] that 
 * uses structured domain identity to detect briefing differences.
 */
class DefaultIntelligenceBriefingChangeAwarenessEngine @Inject constructor() : IntelligenceBriefingChangeAwarenessEngine {

    override fun detectBriefingChanges(
        current: IntelligenceBriefing,
        previous: IntelligenceBriefing?
    ): IntelligenceBriefingChangeSet {
        val now = Instant.now()
        val itemChanges = mutableListOf<IntelligenceBriefingItemChange>()
        
        if (previous == null) {
            return IntelligenceBriefingChangeSet(
                previousBriefingId = null,
                currentBriefingId = current.id,
                itemChanges = current.items.map { 
                    IntelligenceBriefingItemChange(it.intelligenceId, BriefingItemChangeLevel.NEW, null, it.position, "Initial briefing item.")
                },
                hasMeaningfulChange = current.items.isNotEmpty(),
                comparedAt = now
            )
        }

        val previousItemsMap = previous.items.associateBy { it.intelligenceId }
        val currentItemsMap = current.items.associateBy { it.intelligenceId }

        // 1. Detect New and Updated Items
        for (currentItem in current.items) {
            val prevItem = previousItemsMap[currentItem.intelligenceId]
            
            val level = when {
                prevItem == null -> BriefingItemChangeLevel.NEW
                
                // Check for material change from domain result
                currentItem.continuityReference == ContinuityLevel.MATERIAL_CHANGE ||
                currentItem.continuityReference == ContinuityLevel.REVERSAL -> BriefingItemChangeLevel.MATERIALLY_CHANGED
                
                currentItem.continuityReference == ContinuityLevel.REACTIVATED -> BriefingItemChangeLevel.REACTIVATED
                
                // Check for general update (state reference changed but not material change)
                currentItem.currentStateReference != prevItem.currentStateReference -> BriefingItemChangeLevel.UPDATED
                
                else -> BriefingItemChangeLevel.UNCHANGED
            }

            itemChanges.add(
                IntelligenceBriefingItemChange(
                    signalId = currentItem.intelligenceId,
                    level = level,
                    previousPosition = prevItem?.position,
                    currentPosition = currentItem.position,
                    reason = deriveChangeReason(level, currentItem, prevItem),
                    continuityLevel = currentItem.continuityReference
                )
            )
        }

        // 2. Detect Removed Items
        for (prevItem in previous.items) {
            if (!currentItemsMap.containsKey(prevItem.intelligenceId)) {
                itemChanges.add(
                    IntelligenceBriefingItemChange(
                        signalId = prevItem.intelligenceId,
                        level = BriefingItemChangeLevel.REMOVED_FROM_BRIEFING,
                        previousPosition = prevItem.position,
                        currentPosition = null,
                        reason = "Item no longer meets briefing selection criteria."
                    )
                )
            }
        }

        val hasMeaningfulChange = itemChanges.any { it.level != BriefingItemChangeLevel.UNCHANGED }

        return IntelligenceBriefingChangeSet(
            previousBriefingId = previous.id,
            currentBriefingId = current.id,
            itemChanges = itemChanges,
            hasMeaningfulChange = hasMeaningfulChange,
            comparedAt = now
        )
    }

    private fun deriveChangeReason(
        level: BriefingItemChangeLevel, 
        current: IntelligenceBriefingItem, 
        previous: IntelligenceBriefingItem?
    ): String {
        return when (level) {
            BriefingItemChangeLevel.NEW -> "New intelligence added to the briefing."
            BriefingItemChangeLevel.MATERIALLY_CHANGED -> "The underlying intelligence has undergone a material state change."
            BriefingItemChangeLevel.REACTIVATED -> "Previously resolved intelligence topic has been reactivated."
            BriefingItemChangeLevel.UPDATED -> "Authoritative state updated from ${previous?.currentStateReference} to ${current.currentStateReference}."
            BriefingItemChangeLevel.UNCHANGED -> "No change in intelligence state."
            else -> "System identified a change in the briefing representation."
        }
    }
}
