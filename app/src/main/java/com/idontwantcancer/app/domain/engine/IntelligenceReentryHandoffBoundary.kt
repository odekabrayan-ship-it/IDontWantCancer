package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceConsumerIdentity
import com.idontwantcancer.app.domain.model.IntelligenceReentryHandoffResult

/**
 * Interface for the intelligence component responsible for the controlled 
 * handoff of verified lifecycle context downstream.
 */
interface IntelligenceReentryHandoffBoundary {
    /**
     * Performs a controlled handoff of verified re-entry context.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @param consumer The identity of the requesting consumer.
     * @return The structured handoff result.
     */
    suspend fun performHandoff(
        reentryIdentity: String,
        consumer: IntelligenceConsumerIdentity
    ): IntelligenceReentryHandoffResult
}
