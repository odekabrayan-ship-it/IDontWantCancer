package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for determining
 * how to structure a Signal from evaluated decisions.
 */
interface SignalFormationEngine {
    /**
     * Forms a Signal from the provided intelligence decisions.
     *
     * @param significance The evaluated significance decision.
     * @param confidence The evaluated confidence decision.
     * @param conflicts Related conflicts, if any.
     * @param source The source that provided the intelligence.
     * @param material The raw source material.
     * @return The result of the signal formation attempt.
     */
    suspend fun formSignal(
        significance: SignificanceDecision,
        confidence: ConfidenceDecision,
        conflicts: List<IntelligenceConflict>,
        source: IntelligenceSource,
        material: SourceMaterial
    ): SignalFormationResult
}
