package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.ConsolidatedEvent
import com.idontwantcancer.app.domain.model.DeduplicationResult
import com.idontwantcancer.app.domain.model.SourceMaterial

/**
 * Interface for the intelligence component responsible for identifying
 * whether multiple records represent the same underlying event.
 */
interface IntelligenceDeduplicationEngine {
    /**
     * Determines if a piece of source material is a duplicate of an existing event.
     *
     * @param material The material to check.
     * @param existingEvents The list of candidate events from memory.
     * @return The deduplication result.
     */
    fun checkDeduplication(
        material: SourceMaterial,
        existingEvents: List<ConsolidatedEvent>
    ): DeduplicationResult

    /**
     * Consolidates material into an existing or new event.
     */
    fun consolidate(
        material: SourceMaterial,
        existingEvent: ConsolidatedEvent?
    ): ConsolidatedEvent
}
