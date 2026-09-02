package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command result verification.
 */
@Serializable
enum class CommandVerificationStatus {
    /**
     * The command result satisfies the authoritative verification rules.
     */
    VERIFIED,

    /**
     * The command result fails verification (e.g. data mismatch).
     */
    UNVERIFIED,

    /**
     * Verification is pending or established as indeterminate.
     */
    PENDING
}
