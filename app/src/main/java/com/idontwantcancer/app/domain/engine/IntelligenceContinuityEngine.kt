package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for determining 
 * the longitudinal continuity of intelligence states.
 */
interface IntelligenceContinuityEngine {
    /**
     * Evaluates the continuity of a new entry relative to the previous understanding.
     *
     * @param newEntry The new intelligence event.
     * @param signal The associated signal if formed.
     * @param previousState The agency's state prior to this event.
     * @param thread The broader intelligence thread context.
     * @param relations Any supersession relationships for the new entry.
     * @return The determined continuity result.
     */
    fun evaluateContinuity(
        newEntry: TimelineEntry,
        signal: Signal?,
        previousState: ReconstructedState?,
        thread: IntelligenceThread,
        relations: List<SupersessionRelation>
    ): IntelligenceContinuityResult
}
