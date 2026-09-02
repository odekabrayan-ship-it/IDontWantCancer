package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.BriefingExplanation
import com.idontwantcancer.app.domain.model.IntelligenceBriefingItem
import com.idontwantcancer.app.domain.model.Signal

/**
 * Interface for the intelligence component responsible for transforming
 * structured domain data into deterministic, human-readable explanations.
 */
interface IntelligenceBriefingExplanationEngine {
    /**
     * Generates a structured explanation for a briefing item.
     *
     * @param item The briefing item to explain.
     * @param signal The underlying signal context.
     * @return The deterministic explanation.
     */
    suspend fun explain(
        item: IntelligenceBriefingItem,
        signal: Signal
    ): BriefingExplanation
}
