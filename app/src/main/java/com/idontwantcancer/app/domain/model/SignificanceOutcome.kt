package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the decision made by the intelligence significance gate.
 */
@Serializable
enum class SignificanceOutcome {
    /**
     * The change is not significant enough to be processed further.
     */
    NOT_SIGNIFICANT,

    /**
     * The change is meaningful and should proceed to signal formation.
     */
    SIGNIFICANT,

    /**
     * The change is of high significance to public health or safety.
     */
    HIGH_SIGNIFICANCE,

    /**
     * The change represents a critical intelligence event requiring immediate processing.
     */
    CRITICAL
}
