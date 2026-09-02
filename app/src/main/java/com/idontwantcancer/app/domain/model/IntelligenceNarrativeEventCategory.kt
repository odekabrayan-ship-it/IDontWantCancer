package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the structured nature of an event within an intelligence narrative.
 */
@Serializable
enum class IntelligenceNarrativeEventCategory {
    INITIAL_DETECTION,
    CONTINUATION,
    UPDATE,
    MATERIAL_CHANGE,
    REVERSAL,
    REACTIVATION,
    RESOLUTION,
    SUPERSESSION,
    CONFLICT,
    CURRENT_STATE
}
