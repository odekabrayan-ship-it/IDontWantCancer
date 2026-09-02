package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * A coherent, chronologically ordered sequence of narrative events 
 * describing the evolution of an intelligence subject.
 */
@Serializable
data class IntelligenceNarrativeSequence(
    val threadId: String,
    val topicIdentifier: String,
    val events: List<IntelligenceNarrativeEvent>
) {
    /**
     * Deterministically sorted events based on effective time.
     */
    val sortedEvents: List<IntelligenceNarrativeEvent> = events.sortedWith(
        compareBy<IntelligenceNarrativeEvent> { it.effectiveTime }
            .thenBy { it.id }
    )
}
