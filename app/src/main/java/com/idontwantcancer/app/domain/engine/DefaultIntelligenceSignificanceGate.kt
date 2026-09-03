package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceSignificanceGate] that applies deterministic
 * rules to filter out insignificant intelligence changes.
 */
class DefaultIntelligenceSignificanceGate @Inject constructor() : IntelligenceSignificanceGate {

    override fun evaluate(
        change: DetectedChange,
        assessment: EvidenceAssessment,
        source: IntelligenceSource
    ): SignificanceDecision {
        val factors = mutableListOf<String>()
        val now = Instant.now()

        // 1. Filter by Change Type (Noise reduction)
        if (change.type == ChangeType.NO_MEANINGFUL_CHANGE) {
            return SignificanceDecision(
                outcome = SignificanceOutcome.NOT_SIGNIFICANT,
                reason = "No meaningful change was detected in the source material.",
                factors = listOf("ChangeType: NO_MEANINGFUL_CHANGE"),
                decidedAt = now
            )
        }

        // 2. Evaluate Core Significance Factors
        var outcome = SignificanceOutcome.SIGNIFICANT
        
        // High-Value Categories
        if (change.type == ChangeType.SAFETY_ACTION || change.type == ChangeType.REVERSAL) {
            outcome = maxSignificance(outcome, SignificanceOutcome.HIGH_SIGNIFICANCE)
            factors.add("Critical change type: ${change.type}")
        }

        if (change.type == ChangeType.RECOMMENDATION_CHANGE) {
            outcome = maxSignificance(outcome, SignificanceOutcome.HIGH_SIGNIFICANCE)
            factors.add("Official recommendation change")
        }

        // Step 222: Initial Observation of critical categories
        if (change.type == ChangeType.NEW_INFORMATION && 
            (source.type == IntelligenceSourceType.FOOD_SAFETY || source.type == IntelligenceSourceType.PRODUCT_RECALL)) {
            outcome = maxSignificance(outcome, SignificanceOutcome.SIGNIFICANT)
            factors.add("Initial observation of critical health/safety topic")
        }

        // 3. Evidence and Authority Integration
        if (assessment.strength >= EvidenceStrength.HIGH && source.authority == SourceAuthority.INTERNATIONAL) {
            outcome = maxSignificance(outcome, SignificanceOutcome.CRITICAL)
            factors.add("Strong evidence from high-authority international source")
        }

        // 4. Low-Value Rejection
        // Rejection based on weak evidence (Moving logic from SignalFormationEngine)
        if (assessment.strength <= EvidenceStrength.LOW && outcome < SignificanceOutcome.HIGH_SIGNIFICANCE) {
            return SignificanceDecision(
                outcome = SignificanceOutcome.NOT_SIGNIFICANT,
                reason = "The evidence strength is too low for the detected change type.",
                factors = factors + "Insufficient evidence strength: ${assessment.strength}",
                decidedAt = now
            )
        }

        return SignificanceDecision(
            outcome = outcome,
            reason = "Change identified as ${outcome.name} based on type and evidence evaluation.",
            factors = factors,
            decidedAt = now
        )
    }

    private fun maxSignificance(a: SignificanceOutcome, b: SignificanceOutcome): SignificanceOutcome {
        return if (a.ordinal >= b.ordinal) a else b
    }
}
