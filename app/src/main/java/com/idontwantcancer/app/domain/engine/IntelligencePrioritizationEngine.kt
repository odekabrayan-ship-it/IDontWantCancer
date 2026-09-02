package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.PrioritizedSignal
import com.idontwantcancer.app.domain.model.Signal

/**
 * Interface for the intelligence component responsible for determining the
 * relative presentation priority of signals.
 */
interface IntelligencePrioritizationEngine {
    /**
     * Prioritizes a list of signals into a deterministic order based on
     * intelligence characteristics.
     *
     * @param signals The raw signals to prioritize.
     * @return A list of prioritized signals, ordered from highest to lowest priority.
     */
    fun prioritize(signals: List<Signal>): List<PrioritizedSignal>
}
