package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceNarrativeSequence
import com.idontwantcancer.app.domain.model.IntelligenceThread

/**
 * Interface for the intelligence component responsible for reconstructing 
 * the coherent chronological narrative of an intelligence topic.
 */
interface IntelligenceNarrativeContinuityEngine {
    /**
     * Reconstructs the narrative sequence for a specific thread.
     *
     * @param thread The thread context.
     * @return The structured narrative sequence.
     */
    suspend fun reconstructNarrative(thread: IntelligenceThread): IntelligenceNarrativeSequence
}
