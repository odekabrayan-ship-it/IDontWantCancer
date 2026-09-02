package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the collective strength of evidence for an intelligence thread.
 */
@Serializable
enum class EvidenceSynthesisLevel {
    /**
     * Evidence is strong, consistent, and corroborated by multiple authoritative sources.
     */
    SUPPORTED,
    
    STRONGLY_SUPPORTED,

    /**
     * Evidence is developing but lacks independent corroboration.
     */
    LIMITED,

    /**
     * Authoritative sources provide conflicting evidence or interpretations.
     */
    CONTESTED,

    /**
     * Available evidence is insufficient to support an intelligence state.
     */
    INSUFFICIENT,

    /**
     * A previously held conclusion has been formally reversed by new evidence.
     */
    REVERSED
}
