package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines whether an intelligence item is ready to move into the communication layer.
 */
@Serializable
enum class CommunicationReadinessLevel {
    /**
     * Intelligence is settled, well-supported, and ready for authoritative communication.
     */
    READY,

    /**
     * Intelligence is significant but contains inherent uncertainty that must be communicated.
     */
    READY_WITH_UNCERTAINTY,

    /**
     * Intelligence is significant but authoritative sources are in unresolved conflict.
     */
    CONFLICTED,

    /**
     * Intelligence is significant but lacks critical details or evidence dimensions.
     */
    INCOMPLETE,

    /**
     * Intelligence lacks sufficient structure or grounding for accurate communication.
     */
    NOT_READY,

    /**
     * Communication is explicitly blocked due to structural safety or integrity violations.
     */
    BLOCKED
}
