package com.idontwantcancer.app.domain.model

/**
 * Defines the current operational health of an intelligence source.
 * This is separate from the inherent trust or authority of the source.
 */
enum class SourceOperationalStatus {
    UP,
    DOWN,
    DEGRADED,
    UNKNOWN
}
