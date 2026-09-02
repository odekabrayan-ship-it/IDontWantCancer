package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the state of a conflict resolution process.
 */
@Serializable
enum class ResolutionStatus {
    /**
     * The conflict has been identified but not yet resolved.
     */
    UNRESOLVED,

    /**
     * The conflict has been resolved based on available evidence and authority.
     */
    RESOLVED,

    /**
     * The contradiction was apparent only and explained by differences in scope, time, or entity.
     */
    EXPLAINED_BY_CONTEXT,

    /**
     * One or more competing intelligence items have been superseded by more authoritative or recent data.
     */
    SUPERSEDED,

    /**
     * There is not enough information to resolve the conflict at this time.
     */
    INSUFFICIENT_INFORMATION
}
