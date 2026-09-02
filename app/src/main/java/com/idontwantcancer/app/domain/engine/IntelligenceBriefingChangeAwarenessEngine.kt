package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import com.idontwantcancer.app.domain.model.IntelligenceBriefingChangeSet

/**
 * Interface for the intelligence component responsible for identifying 
 * structured changes between briefing versions.
 */
interface IntelligenceBriefingChangeAwarenessEngine {
    /**
     * Compares the current briefing with a previous version.
     *
     * @param current The current assembled briefing.
     * @param previous The previous briefing version, or null if none exists.
     * @return The structured change set.
     */
    fun detectBriefingChanges(
        current: IntelligenceBriefing,
        previous: IntelligenceBriefing?
    ): IntelligenceBriefingChangeSet
}
