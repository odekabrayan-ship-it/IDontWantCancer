package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import com.idontwantcancer.app.domain.model.PrioritizedSignal
import java.time.Instant

/**
 * Interface for the intelligence component responsible for assembling the most
 * important current intelligence into a coherent briefing for the user.
 */
interface IntelligenceBriefingAssemblyEngine {
    /**
     * Assembles a briefing from the provided prioritized signals.
     *
     * @param prioritizedSignals The list of signals evaluated for priority.
     * @param atTime The timestamp for the briefing generation.
     * @return The assembled intelligence briefing.
     */
    suspend fun assembleBriefing(
        prioritizedSignals: List<PrioritizedSignal>,
        atTime: Instant
    ): IntelligenceBriefing
}
