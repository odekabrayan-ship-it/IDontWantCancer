package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the nature of a deterministic state transition.
 */
@Serializable
enum class IntelligenceStateTransitionType {
    /**
     * Initial state creation from first observation.
     */
    INITIALIZED,

    /**
     * State was updated with compatible new information.
     */
    UPDATED,

    /**
     * State was materially corrected based on new authoritative data.
     */
    CORRECTED,

    /**
     * State was confirmed by additional independent evidence.
     */
    CONFIRMED,

    /**
     * State was replaced by a more authoritative or current finding.
     */
    REPLACED,

    /**
     * State was contested due to conflicting authoritative reports.
     */
    CONTESTED,

    /**
     * Previous conflict was resolved into a new authoritative state.
     */
    RESOLVED,
    
    RETRACTED
}
