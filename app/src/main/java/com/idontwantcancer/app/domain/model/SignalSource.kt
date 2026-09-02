package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the origin of a cancer-intelligence signal.
 */
@Serializable
data class SignalSource(
    val name: String,
    val url: String? = null
)
