package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceCommunicationPackageIntegrityEngine] that 
 * applies deterministic consistency rules to verify communication packages.
 */
class DefaultIntelligenceCommunicationPackageIntegrityEngine @Inject constructor() : 
    IntelligenceCommunicationPackageIntegrityEngine {

    override fun verifyPackageIntegrity(
        pkg: IntelligenceCommunicationPackage
    ): IntelligenceCommunicationIntegrityResult {
        val now = Instant.now()
        val violations = mutableListOf<CommunicationIntegrityViolation>()

        // 1. Identity Consistency
        if (pkg.intelligenceId != pkg.provenance.signalId) {
            violations.add(
                CommunicationIntegrityViolation(
                    type = CommunicationIntegrityViolationType.IDENTITY_MISMATCH,
                    description = "Package intelligence ID does not match provenance signal ID.",
                    expectedValue = pkg.provenance.signalId,
                    actualValue = pkg.intelligenceId
                )
            )
        }

        // 2. Thread Consistency
        if (pkg.threadId != pkg.currentState.threadId) {
            violations.add(
                CommunicationIntegrityViolation(
                    type = CommunicationIntegrityViolationType.THREAD_MISMATCH,
                    description = "Package thread ID does not match current state thread ID.",
                    expectedValue = pkg.currentState.threadId,
                    actualValue = pkg.threadId
                )
            )
        }

        // 3. Evidence Consistency (Value match)
        if (pkg.confidence != pkg.provenance.signal.confidence) {
            violations.add(
                CommunicationIntegrityViolation(
                    type = CommunicationIntegrityViolationType.EVIDENCE_MISMATCH,
                    description = "Package confidence level contradicts provenance signal confidence.",
                    expectedValue = pkg.provenance.signal.confidence.name,
                    actualValue = pkg.confidence?.name
                )
            )
        }

        // 4. Significance Consistency
        if (pkg.significanceLevel != pkg.provenance.signal.significanceLevel) {
            violations.add(
                CommunicationIntegrityViolation(
                    type = CommunicationIntegrityViolationType.EVIDENCE_MISMATCH,
                    description = "Package significance level contradicts provenance signal significance.",
                    expectedValue = pkg.provenance.signal.significanceLevel?.name,
                    actualValue = pkg.significanceLevel?.name
                )
            )
        }

        // 5. State Consistency (Authoritative state)
        // Verify current state ID matches package current state entry
        if (pkg.currentState.effectiveEntryId.isBlank()) {
            violations.add(
                CommunicationIntegrityViolation(
                    type = CommunicationIntegrityViolationType.STATE_MISMATCH,
                    description = "Package current state effective entry ID is missing."
                )
            )
        }

        // 6. Conflict Consistency
        val hasConflict = pkg.conflicts.any { it.resolutionStatus == ResolutionStatus.UNRESOLVED }
        if (hasConflict && !pkg.communicationReadiness.requiresQualification && pkg.communicationReadiness.level == CommunicationReadinessLevel.READY) {
            violations.add(
                CommunicationIntegrityViolation(
                    type = CommunicationIntegrityViolationType.CONFLICT_MISSING,
                    description = "Package contains unresolved conflict but readiness is set to READY without qualification."
                )
            )
        }

        // 7. Provenance Completeness
        if (pkg.provenance.sources.isEmpty()) {
            violations.add(
                CommunicationIntegrityViolation(
                    type = CommunicationIntegrityViolationType.PROVENANCE_MISSING,
                    description = "Package provenance contains no identifiable source materials."
                )
            )
        }

        val status = if (violations.isEmpty()) CommunicationIntegrityStatus.VALID else CommunicationIntegrityStatus.INVALID

        return IntelligenceCommunicationIntegrityResult(
            packageId = pkg.intelligenceId, // Use intelligenceId as package identifier proxy
            intelligenceId = pkg.intelligenceId,
            status = status,
            violations = violations,
            checkedAt = now
        )
    }
}
