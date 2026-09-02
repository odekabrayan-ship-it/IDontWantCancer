package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Factory responsible for enforcing the validation gate and constructing 
 * the immutable handoff contract.
 */
class IntelligenceCommunicationHandoffFactory @Inject constructor(
    private val safetyGate: IntelligenceCommunicationSafetyGate,
    private val integrityEngine: IntelligenceCommunicationPackageIntegrityEngine
) {
    /**
     * Validates and constructs a handoff contract for the provided package.
     *
     * @param pkg The candidate communication package.
     * @return The structured handoff result (Success or Failure).
     */
    fun createHandoff(pkg: IntelligenceCommunicationPackage): HandoffResult {
        val now = Instant.now()

        // 1. Enforce Communication Safety Gate (Step 69)
        val safetyResult = safetyGate.evaluateSafety(pkg)
        if (safetyResult.level == CommunicationReadinessLevel.BLOCKED || 
            safetyResult.level == CommunicationReadinessLevel.NOT_READY) {
            return HandoffResult.Failure(
                reason = "Safety Gate Violation: ${safetyResult.reason}",
                readinessLevel = safetyResult.level
            )
        }

        // 2. Enforce Package Integrity (Step 70)
        val integrityResult = integrityEngine.verifyPackageIntegrity(pkg)
        if (integrityResult.status == CommunicationIntegrityStatus.INVALID) {
            return HandoffResult.Failure(
                reason = "Integrity Violation: ${integrityResult.violations.firstOrNull()?.description}",
                readinessLevel = safetyResult.level,
                integrityStatus = integrityResult.status
            )
        }

        // 3. Construct Authorized Handoff (Fail-Closed)
        return HandoffResult.Success(
            IntelligenceCommunicationHandoff(
                packageId = pkg.intelligenceId,
                intelligenceId = pkg.intelligenceId,
                threadId = pkg.threadId,
                communicationPackage = pkg.copy(communicationReadiness = safetyResult),
                authorizedAt = now
            )
        )
    }
}
