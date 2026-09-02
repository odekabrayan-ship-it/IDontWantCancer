package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the structured outcome of crossing the re-entry recovery boundary.
 * Only verified lifecycles are permitted to proceed to downstream evaluation.
 */
@Serializable
sealed interface ReentryRecoveryResult {
    /**
     * The re-entry lifecycle has been verified and is safe for consumption.
     */
    @Serializable
    data class Verified(val lifecycle: IntelligenceReentryLifecycle) : ReentryRecoveryResult

    /**
     * The re-entry lifecycle failed integrity verification or was not found.
     */
    @Serializable
    data class Unverified(
        val identity: String,
        val integrityResult: IntelligenceReentryIntegrityResult? = null,
        val reason: String
    ) : ReentryRecoveryResult
}
