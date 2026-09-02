package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import javax.inject.Inject

/**
 * Default implementation of the recovery boundary that enforces strict 
 * integrity checks before exposing re-entry state to downstream consumers.
 */
class DefaultIntelligenceReentryRecoveryBoundary @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val integrityGate: IntelligenceReentryIntegrityGate
) : IntelligenceReentryRecoveryBoundary {

    override suspend fun getVerifiedReentry(reentryIdentity: String): ReentryRecoveryResult {
        // 1. Perform Integrity and Trust Check (Step 82)
        val integrity = integrityGate.verifyIntegrity(reentryIdentity)

        if (integrity.status == ReentryIntegrityStatus.INTEGRITY_FAILED) {
            return ReentryRecoveryResult.Unverified(
                identity = reentryIdentity,
                integrityResult = integrity,
                reason = "Recovery failed: Lifecycle integrity could not be confirmed."
            )
        }

        // 2. Retrieve authoritative state after verification
        val lifecycle = memory.getReentryLifecycleByIdentity(reentryIdentity)

        return if (lifecycle != null) {
            ReentryRecoveryResult.Verified(lifecycle)
        } else {
            ReentryRecoveryResult.Unverified(
                identity = reentryIdentity,
                integrityResult = integrity,
                reason = "Recovery failed: Ledger record missing for verified identity."
            )
        }
    }
}
