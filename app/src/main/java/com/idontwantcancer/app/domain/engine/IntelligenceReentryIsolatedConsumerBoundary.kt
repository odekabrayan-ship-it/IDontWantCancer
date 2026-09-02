package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceConsumerIdentity
import com.idontwantcancer.app.domain.model.IntelligenceReentryHandoffConsumptionContract

/**
 * Narrow, read-only interface that provides isolated access to verified 
 * re-entry lifecycle information for authorized downstream consumers.
 * Ensures that lifecycle authority remains isolated within the core domain.
 */
interface IntelligenceReentryIsolatedConsumerBoundary {
    /**
     * Obtains an isolated consumption contract for an authorized consumer.
     *
     * @param reentryIdentity The unique identity of the re-entry event.
     * @param consumer The identity of the requesting consumer.
     * @return The verified contract if admitted, or null if denied/not found.
     */
    suspend fun getIsolatedContract(
        reentryIdentity: String,
        consumer: IntelligenceConsumerIdentity
    ): IntelligenceReentryHandoffConsumptionContract?
}
