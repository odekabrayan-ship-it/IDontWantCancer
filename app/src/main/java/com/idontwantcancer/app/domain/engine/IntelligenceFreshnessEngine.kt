package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.FreshnessStatus
import com.idontwantcancer.app.domain.model.IntelligenceEventTimeline
import com.idontwantcancer.app.domain.model.IntelligenceThread
import com.idontwantcancer.app.domain.model.TimelineEntry

/**
 * Interface for the intelligence component responsible for determining 
 * the temporal status (freshness/staleness) of intelligence items.
 */
interface IntelligenceFreshnessEngine {
    /**
     * Evaluates the freshness of a specific timeline entry within its chronological context.
     *
     * @param entry The entry to evaluate.
     * @param timeline The full historical timeline for the relevant topic.
     * @param thread The broader intelligence thread context.
     * @return The determined freshness status.
     */
    fun evaluateFreshness(
        entry: TimelineEntry,
        timeline: IntelligenceEventTimeline,
        thread: IntelligenceThread
    ): FreshnessStatus
}
