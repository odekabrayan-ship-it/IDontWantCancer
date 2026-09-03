package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the geographical reach of an intelligence signal or source.
 */
@Serializable
enum class GeographicScope {
    /**
     * Applies to all humans regardless of location (e.g., Global Research, WHO guidelines).
     */
    GLOBAL,

    /**
     * Applies to a specific continent or economic region (e.g., European Union).
     */
    REGIONAL,

    /**
     * Applies to a specific country (e.g., Local FDA recalls).
     */
    NATIONAL
}
