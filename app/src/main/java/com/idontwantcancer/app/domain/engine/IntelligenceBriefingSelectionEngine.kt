package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceBriefingSelection
import com.idontwantcancer.app.domain.model.IntelligenceCommunicationPackage
import com.idontwantcancer.app.domain.model.PrioritizedSignal

/**
 * Interface for the intelligence component responsible for deterministically
 * selecting and ordering the items for a briefing.
 */
interface IntelligenceBriefingSelectionEngine {
    /**
     * Selects and orders briefing items from a collection of candidate packages.
     *
     * @param candidates The list of communication packages and their priority context.
     * @return The structured selection result.
     */
    fun selectBriefing(
        candidates: List<Pair<PrioritizedSignal, IntelligenceCommunicationPackage>>
    ): IntelligenceBriefingSelection
}
