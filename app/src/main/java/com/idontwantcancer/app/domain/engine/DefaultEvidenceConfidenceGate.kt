package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [EvidenceConfidenceGate] that applies deterministic
 * rules to evaluate evidence confidence.
 */
class DefaultEvidenceConfidenceGate @Inject constructor() : EvidenceConfidenceGate {

    override fun evaluate(
        assessment: EvidenceAssessment,
        source: IntelligenceSource,
        conflicts: List<IntelligenceConflict>
    ): ConfidenceDecision {
        val factors = assessment.factors.toMutableList()
        val hasUnresolvedConflicts = conflicts.any { it.resolutionStatus == ResolutionStatus.UNRESOLVED }
        
        // 1. Map EvidenceStrength to SignalConfidence
        var confidence = when (assessment.strength) {
            EvidenceStrength.VERY_HIGH -> SignalConfidence.VERY_HIGH
            EvidenceStrength.HIGH -> SignalConfidence.HIGH
            EvidenceStrength.MODERATE -> SignalConfidence.MODERATE
            EvidenceStrength.LOW, EvidenceStrength.VERY_LOW -> SignalConfidence.LOW
        }

        // 2. Adjust for Unresolved Conflicts
        if (hasUnresolvedConflicts) {
            factors.add(
                EvidenceFactor(
                    name = "Unresolved Conflict",
                    description = "Competing authoritative sources provide differing information",
                    impact = FactorImpact.NEGATIVE
                )
            )
            confidence = confidence.downgrade()
        }

        // 3. Adjust for Source Correction State
        if (assessment.change.type == ChangeType.CORRECTION) {
            factors.add(
                EvidenceFactor(
                    name = "Source Correction",
                    description = "Source is revising previously issued information",
                    impact = FactorImpact.NEUTRAL
                )
            )
            // Corrections often increase transparency but can temporarily lower confidence 
            // until the new state is corroborated.
            confidence = confidence.downgrade()
        }

        // 4. Adjust for Source Reliability metadata
        if (source.reliability.trustLevel <= SourceTrustLevel.LOW) {
            factors.add(
                EvidenceFactor(
                    name = "Source Scrutiny",
                    description = "Source reliability metadata suggests caution",
                    impact = FactorImpact.NEGATIVE
                )
            )
            confidence = confidence.downgrade()
        }

        return ConfidenceDecision(
            confidence = confidence,
            factors = factors,
            assessedAt = Instant.now(),
            hasUnresolvedConflicts = hasUnresolvedConflicts
        )
    }

    private fun SignalConfidence.downgrade(): SignalConfidence {
        return when (this) {
            SignalConfidence.VERY_HIGH -> SignalConfidence.HIGH
            SignalConfidence.HIGH -> SignalConfidence.MODERATE
            SignalConfidence.MODERATE -> SignalConfidence.LOW
            SignalConfidence.LOW -> SignalConfidence.LOW
        }
    }
}
