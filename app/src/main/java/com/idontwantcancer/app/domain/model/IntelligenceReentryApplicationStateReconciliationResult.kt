package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The structured result of reconciling re-entry completion with current application state.
 */
@Serializable
data class IntelligenceReentryApplicationStateReconciliationResult(
    val reentryIdentity: String,
    val status: ApplicationStateReconciliationStatus,
    val details: String? = null,
    @Serializable(with = InstantSerializer::class)
    val verifiedAt: Instant
) {
    /**
     * Maps this result to a narrow consumption contract.
     */
    fun toConsumptionContract(): IntelligenceReentryReconciliationConsumptionContract {
        return IntelligenceReentryReconciliationConsumptionContract(
            reentryIdentity = reentryIdentity,
            isConsistent = status == ApplicationStateReconciliationStatus.CONSISTENT,
            verifiedAt = verifiedAt,
            detail = details
        )
    }
}
