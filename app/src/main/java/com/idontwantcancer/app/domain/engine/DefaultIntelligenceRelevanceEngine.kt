package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceRelevanceEngine] that applies deterministic
 * identity and scope rules to evaluate information relevance.
 */
class DefaultIntelligenceRelevanceEngine @Inject constructor() : IntelligenceRelevanceEngine {

    override fun evaluateRelevance(
        material: SourceMaterial,
        existingThreads: List<IntelligenceThread>
    ): IntelligenceRelevanceResult {
        // Rule: Conservative matching based on Topic Identifier (Stable Entity/Subject ID)
        val match = existingThreads.find { 
            it.topicIdentifier.equals(material.title, ignoreCase = true) 
        }

        if (match == null) {
            return IntelligenceRelevanceResult(
                level = RelevanceLevel.NOT_RELEVANT,
                reason = "No existing thread matches the topic identifier: ${material.title}"
            )
        }

        // Rule: Scope Check (Conceptual)
        // If we had structured scope data, we would compare it here.
        // For now, we assume matching the topic identifier establishes basic relevance.
        
        return IntelligenceRelevanceResult(
            level = RelevanceLevel.RELEVANT,
            reason = "Direct match established via topic identifier.",
            matchingThreadId = match.id
        )
    }
}
