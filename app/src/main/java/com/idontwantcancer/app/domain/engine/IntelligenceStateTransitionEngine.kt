package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for determining 
 * how the agency's current state transitions in response to new information.
 */
interface IntelligenceStateTransitionEngine {
    /**
     * Determines the state transition resulting from a new timeline entry.
     *
     * @param previousState The agency's understanding prior to the new entry.
     * @param newEntry The new validated intelligence event.
     * @param relations Any supersession relationships identified for the new entry.
     * @param conflicts Current conflict state related to the topic.
     * @param synthesis The synthesized evidence result for the topic.
     * @return The resulting state transition, or null if no state change occurs.
     */
    fun evaluateTransition(
        previousState: ReconstructedState?,
        newEntry: TimelineEntry,
        relations: List<SupersessionRelation>,
        conflicts: List<IntelligenceConflict>,
        synthesis: EvidenceSynthesisResult
    ): IntelligenceStateTransition?
}
