package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceCommunicationHandoff
import com.idontwantcancer.app.domain.model.IntelligenceCommunicationSelectionResult

/**
 * Interface for the intelligence component responsible for determining 
 * which validated items are eligible for the current communication cycle.
 */
interface IntelligenceCommunicationSelectionGate {
    /**
     * Filters a list of ordered handoffs for cycle eligibility.
     *
     * @param orderedHandoffs The handoffs in their deterministic order.
     * @return The structured selection result.
     */
    fun selectEligible(
        orderedHandoffs: List<IntelligenceCommunicationHandoff>
    ): IntelligenceCommunicationSelectionResult
}
