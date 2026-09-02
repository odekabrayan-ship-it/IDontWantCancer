package com.idontwantcancer.app.domain.model

/**
 * Defines the impact of an integrity violation on the intelligence pipeline.
 */
enum class IntegrityViolationSeverity {
    /**
     * Minor inconsistency that does not prevent processing but should be audited.
     */
    INFO,

    /**
     * Significant issue that may affect characterization but does not invalidate the item.
     */
    WARNING,

    /**
     * Critical failure that invalidates the intelligence item or its state.
     */
    ERROR
}
