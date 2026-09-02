package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the structured outcome of querying a re-entry audit history 
 * through the verified recovery boundary.
 */
@Serializable
sealed interface ReentryHistoryResult {
    /**
     * The re-entry history has been verified and is safe for consumption.
     */
    @Serializable
    data class Verified(val history: List<IntelligenceReentryAuditEntry>) : ReentryHistoryResult

    /**
     * The re-entry history could not be verified due to integrity or missing data.
     */
    @Serializable
    data class Unverified(
        val identity: String,
        val reason: String
    ) : ReentryHistoryResult
}
