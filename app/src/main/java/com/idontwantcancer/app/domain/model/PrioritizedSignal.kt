package com.idontwantcancer.app.domain.model

/**
 * A wrapper for a [Signal] that includes intelligence-driven priority information.
 */
data class PrioritizedSignal(
    val signal: Signal,
    val attentionLevel: AttentionLevel,
    val rankingScore: Int // Deterministic score for ordering within the same attention level
)
