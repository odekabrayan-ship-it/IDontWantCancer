package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceRelevanceResult
import com.idontwantcancer.app.domain.model.IntelligenceThread
import com.idontwantcancer.app.domain.model.SourceMaterial

/**
 * Interface for the intelligence component responsible for determining 
 * the relevance and scope of information within the agency's context.
 */
interface IntelligenceRelevanceEngine {
    /**
     * Evaluates a piece of material against existing intelligence threads.
     *
     * @param material The material to evaluate.
     * @param existingThreads The candidates for association.
     * @return The relevance and scope result.
     */
    fun evaluateRelevance(
        material: SourceMaterial,
        existingThreads: List<IntelligenceThread>
    ): IntelligenceRelevanceResult
}
