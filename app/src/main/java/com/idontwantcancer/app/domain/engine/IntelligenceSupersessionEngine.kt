package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.SupersessionRelation
import com.idontwantcancer.app.domain.model.TimelineEntry

/**
 * Interface for the intelligence component responsible for determining 
 * whether one intelligence state replaces or updates another.
 */
interface IntelligenceSupersessionEngine {
    /**
     * Identifies supersession relationships for a new entry within its timeline.
     *
     * @param newEntry The newly created timeline entry.
     * @param existingEntries The existing chronological timeline for the topic.
     * @return A list of identified supersession relationships.
     */
    fun detectSupersession(
        newEntry: TimelineEntry,
        existingEntries: List<TimelineEntry>
    ): List<SupersessionRelation>
}
