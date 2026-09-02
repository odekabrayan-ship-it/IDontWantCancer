package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the nature of a missing piece of critical intelligence.
 */
@Serializable
enum class EvidenceGapLevel {
    /**
     * No structural gaps have been identified for this topic.
     */
    NO_IDENTIFIED_GAP,

    /**
     * Fundamental evidence supporting the claim is missing.
     */
    INSUFFICIENT_SUPPORT,

    /**
     * Conflicting evidence exists with no resolution path yet.
     */
    UNRESOLVED_CONTRADICTION,
    
    UNRESOLVED_CONFLICT,

    /**
     * Evidence is available but does not cover the required scope (e.g., specific population).
     */
    MISSING_SCOPE_COVERAGE,

    /**
     * Authoritative confirmation is pending from primary regulators.
     */
    PENDING_AUTHORITATIVE_CONFIRMATION,
    
    MISSING_CORROBORATION
}
