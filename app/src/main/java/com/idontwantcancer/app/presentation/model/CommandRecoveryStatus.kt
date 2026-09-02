package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command recovery operation.
 */
@Serializable
enum class CommandRecoveryStatus {
    /**
     * Recovery is not required for this command outcome.
     */
    RECOVERY_NOT_REQUIRED,

    /**
     * Recovery is permitted and has been requested from the existing authority.
     */
    RECOVERY_PERMITTED,

    /**
     * Recovery was attempted but failed to restore consistent state.
     */
    RECOVERY_FAILED,

    /**
     * Recovery successfully restored consistent state according to authoritative verification.
     */
    RECOVERED
}
