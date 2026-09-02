package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceEventTimeline
import com.idontwantcancer.app.domain.model.TimelineEntry

/**
 * Interface for the intelligence component responsible for reconstructing 
 * timelines and deriving the current state of an intelligence topic.
 */
interface TimelineReconstructor {
    /**
     * Constructs a formal timeline from a set of raw timeline entries.
     */
    fun reconstruct(
        threadId: String,
        topicIdentifier: String,
        entries: List<TimelineEntry>
    ): IntelligenceEventTimeline

    /**
     * Derives a summary of the current state of the intelligence topic 
     * based on its historical timeline.
     */
    fun deriveCurrentStateSummary(timeline: IntelligenceEventTimeline): String
}
