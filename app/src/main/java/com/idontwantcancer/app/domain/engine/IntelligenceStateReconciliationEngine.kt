package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for determining the 
 * authoritative current state of an intelligence context.
 */
interface IntelligenceStateReconciliationEngine {
    /**
     * Reconciles the existing state with new intelligence and relationships.
     *
     * @param thread The thread context.
     * @param previousState The previous authoritative state.
     * @param newState The candidate new state (reconstructed from timeline).
     * @param continuity The determined continuity relationship.
     * @param transition The state transition identified.
     * @return The reconciliation result.
     */
    fun reconcileState(
        thread: IntelligenceThread,
        previousState: ReconstructedState?,
        newState: ReconstructedState,
        continuity: IntelligenceContinuityResult,
        transition: IntelligenceStateTransition?
    ): IntelligenceStateReconciliationResult
}
