package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [EvidenceAssessmentEngine] that evaluates evidence
 * based on source authority, reliability, and corroboration levels.
 */
class DefaultEvidenceAssessmentEngine @Inject constructor() : EvidenceAssessmentEngine {

    override suspend fun assess(
        change: DetectedChange,
        primarySource: IntelligenceSource,
        corroboratingSources: List<IntelligenceSource>
    ): EvidenceAssessment {
        val factors = mutableListOf<EvidenceFactor>()
        
        // 1. Evaluate Primary Source Authority & Trust
        var baseStrength = when (primarySource.authority) {
            SourceAuthority.INTERNATIONAL,
            SourceAuthority.REGULATORY_BODY -> EvidenceStrength.HIGH
            SourceAuthority.NATIONAL -> EvidenceStrength.MODERATE
            SourceAuthority.REGIONAL,
            SourceAuthority.INDEPENDENT_ACADEMIC -> EvidenceStrength.LOW
        }

        factors.add(
            EvidenceFactor(
                "Source Authority", 
                "Source is classified as ${primarySource.authority}", 
                FactorImpact.POSITIVE
            )
        )

        // Adjust based on Inherent Trust Level
        when (primarySource.reliability.trustLevel) {
            SourceTrustLevel.VERY_HIGH -> {
                baseStrength = baseStrength.upgrade()
                factors.add(EvidenceFactor("Exceptional Trust", "Source has a verified track record of accuracy", FactorImpact.POSITIVE))
            }
            SourceTrustLevel.HIGH -> {
                // Keep as is or slight boost
                factors.add(EvidenceFactor("High Trust", "Source is considered highly reliable", FactorImpact.POSITIVE))
            }
            SourceTrustLevel.VERY_LOW, SourceTrustLevel.LOW -> {
                baseStrength = baseStrength.downgrade()
                factors.add(EvidenceFactor("Limited Reliability", "Source metadata suggests additional scrutiny is required", FactorImpact.NEGATIVE))
            }
            else -> {}
        }

        // Adjust for specialization
        if (primarySource.reliability.specialization != null) {
            factors.add(EvidenceFactor("Specialized Source", "Source specializes in ${primarySource.reliability.specialization}", FactorImpact.NEUTRAL))
        }

        // 2. Evaluate Corroboration
        var finalStrength = baseStrength
        if (corroboratingSources.isNotEmpty()) {
            factors.add(
                EvidenceFactor(
                    "Corroboration", 
                    "Supported by ${corroboratingSources.size} additional sources", 
                    FactorImpact.POSITIVE
                )
            )
            
            finalStrength = when {
                corroboratingSources.size >= 3 -> finalStrength.upgrade().upgrade()
                else -> finalStrength.upgrade()
            }
        } else {
            factors.add(EvidenceFactor("Single Source", "No corroborating sources detected yet", FactorImpact.NEUTRAL))
        }

        return EvidenceAssessment(
            change = change,
            strength = finalStrength,
            factors = factors,
            assessedAt = Instant.now(),
            isConsistent = true,
            hasUncertainty = finalStrength < EvidenceStrength.MODERATE
        )
    }

    private fun EvidenceStrength.upgrade(): EvidenceStrength {
        return when (this) {
            EvidenceStrength.VERY_LOW -> EvidenceStrength.LOW
            EvidenceStrength.LOW -> EvidenceStrength.MODERATE
            EvidenceStrength.MODERATE -> EvidenceStrength.HIGH
            EvidenceStrength.HIGH -> EvidenceStrength.VERY_HIGH
            EvidenceStrength.VERY_HIGH -> EvidenceStrength.VERY_HIGH
        }
    }

    private fun EvidenceStrength.downgrade(): EvidenceStrength {
        return when (this) {
            EvidenceStrength.VERY_HIGH -> EvidenceStrength.HIGH
            EvidenceStrength.HIGH -> EvidenceStrength.MODERATE
            EvidenceStrength.MODERATE -> EvidenceStrength.LOW
            EvidenceStrength.LOW -> EvidenceStrength.VERY_LOW
            EvidenceStrength.VERY_LOW -> EvidenceStrength.VERY_LOW
        }
    }
}
