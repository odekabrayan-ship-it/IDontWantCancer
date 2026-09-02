package com.idontwantcancer.app.domain.model

/**
 * Defines structured reasons why a piece of intelligence was excluded from a briefing.
 */
enum class SelectionExclusionReason {
    /**
     * The intelligence did not meet the required significance threshold.
     */
    NOT_SIGNIFICANT,

    /**
     * The intelligence is not in a state ready for communication.
     */
    NOT_COMMUNICATION_READY,

    /**
     * The intelligence has been superseded by newer authoritative information.
     */
    SUPERSEDED,

    /**
     * The intelligence is a duplicate of another item already in the briefing.
     */
    DUPLICATE,

    /**
     * The intelligence applies to a scope that is currently excluded.
     */
    OUTSIDE_SCOPE,

    /**
     * The intelligence record is internally inconsistent or invalid.
     */
    INVALID,

    /**
     * The intelligence is no longer fresh enough for a current briefing.
     */
    NOT_CURRENT
}
