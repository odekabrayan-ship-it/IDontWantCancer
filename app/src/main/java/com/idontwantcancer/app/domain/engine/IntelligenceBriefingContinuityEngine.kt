package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for determining 
 * the structured continuity relationship across briefings.
 */
interface IntelligenceBriefingContinuityEngine {
    /**
     * Determines the continuity result for a specific briefing item.
     *
     * @param currentBriefingId The ID of the current briefing.
     * @param previousBriefingId The ID of the previous briefing.
     * @param itemChange The change awareness result for the item.
     * @param pkg The communication package for the current item.
     * @return The structured continuity result.
     */
    fun determineContinuity(
        currentBriefingId: String,
        previousBriefingId: String?,
        itemChange: IntelligenceBriefingItemChange,
        pkg: IntelligenceCommunicationPackage
    ): IntelligenceBriefingContinuityResult
}
