package com.idontwantcancer.app.domain.model

/**
 * Defines the lifecycle states an intelligence unit move through within the agency.
 */
enum class IntelligenceLifecycle {
    DETECTED,
    ASSESSED,
    FORMED,
    PRIORITIZED,
    BRIEFED,
    ARCHIVED
}
