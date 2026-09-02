package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceEventTimeline
import com.idontwantcancer.app.domain.model.ReconstructedState

/**
 * Interface for the intelligence component responsible for reconstructing 
 * the current state of knowledge from a chronological timeline of events.
 */
interface IntelligenceStateReconstructionEngine {
    /**
     * Reconstructs the current state for the provided timeline.
     *
     * @param timeline The historical timeline of intelligence events.
     * @return The derived current state.
     */
    suspend fun reconstructCurrentState(timeline: IntelligenceEventTimeline): ReconstructedState
}
