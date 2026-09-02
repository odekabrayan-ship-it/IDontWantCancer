package com.idontwantcancer.app.domain.model

/**
 * Represents the structured reliability characteristics of an intelligence source.
 */
data class SourceReliability(
    val trustLevel: SourceTrustLevel,
    val isTransparent: Boolean,
    val updateReliability: SourceTrustLevel,
    val specialization: SignalCategory? = null
)
