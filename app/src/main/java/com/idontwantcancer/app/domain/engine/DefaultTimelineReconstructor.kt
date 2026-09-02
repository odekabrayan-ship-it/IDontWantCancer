package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceEventTimeline
import com.idontwantcancer.app.domain.model.TimelineEntry
import com.idontwantcancer.app.domain.model.TimelineEntryType
import javax.inject.Inject

/**
 * Default implementation of [TimelineReconstructor] that uses deterministic
 * chronological rules to rebuild the evolution of an intelligence topic.
 */
class DefaultTimelineReconstructor @Inject constructor() : TimelineReconstructor {

    override fun reconstruct(
        threadId: String,
        topicIdentifier: String,
        entries: List<TimelineEntry>
    ): IntelligenceEventTimeline {
        return IntelligenceEventTimeline(
            threadId = threadId,
            topicIdentifier = topicIdentifier,
            entries = entries
        )
    }

    override fun deriveCurrentStateSummary(timeline: IntelligenceEventTimeline): String {
        val sorted = timeline.sortedEntries
        if (sorted.isEmpty()) return "No historical intelligence available."

        // The current state is the result of the chronological evolution.
        // We look for reversals or resolutions first as they represent terminal states.
        val lastResolution = sorted.lastOrNull { it.type == TimelineEntryType.RESOLUTION }
        if (lastResolution != null) return lastResolution.description

        val lastReversal = sorted.lastOrNull { it.type == TimelineEntryType.REVERSAL }
        if (lastReversal != null) return lastReversal.description

        // Otherwise, the latest meaningful entry defines the current understanding.
        return sorted.last().description
    }
}
