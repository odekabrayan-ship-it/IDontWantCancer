package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the authoritative identities of downstream domain consumers 
 * authorized to receive verified lifecycle information.
 */
@Serializable
enum class IntelligenceConsumerIdentity {
    /**
     * The component responsible for assembling the daily intelligence briefing.
     */
    BRIEFING_ASSEMBLY,

    /**
     * The component responsible for background intelligence synchronization.
     */
    INTELLIGENCE_SYNC,

    /**
     * The component responsible for evaluating re-entry significance.
     */
    SIGNIFICANCE_EVALUATION
}
