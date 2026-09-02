package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.util.UUID
import javax.inject.Inject

/**
 * Default implementation of [SignalFormationEngine] that structures a Signal
 * using evaluated significance and confidence decisions.
 */
class DefaultSignalFormationEngine @Inject constructor() : SignalFormationEngine {

    override suspend fun formSignal(
        significance: SignificanceDecision,
        confidence: ConfidenceDecision,
        conflicts: List<IntelligenceConflict>,
        source: IntelligenceSource,
        material: SourceMaterial
    ): SignalFormationResult {
        
        // 1. Determine Signal Importance (Base logic)
        val importance = calculateImportance(significance, source)

        // 2. Resolve Conflict Status for Signal
        val conflictStatus = conflicts.firstOrNull()?.resolutionStatus

        // 3. Create Signal
        val signal = Signal(
            id = UUID.randomUUID().toString(),
            title = material.title,
            summary = material.content.take(200),
            significance = significance.reason,
            significanceLevel = significance.outcome,
            significanceFactors = significance.factors,
            explanation = null,
            category = mapSourceTypeToCategory(source.type),
            importance = importance,
            confidence = confidence.confidence,
            confidenceFactors = confidence.factors,
            conflictStatus = conflictStatus,
            detectedAt = confidence.assessedAt,
            publishedAt = material.publishedAt,
            recommendedAction = null,
            source = SignalSource(
                name = source.name,
                url = material.canonicalUrl
            )
        )

        return SignalFormationResult.SignalCreated(signal)
    }

    private fun calculateImportance(
        significance: SignificanceDecision,
        source: IntelligenceSource
    ): SignalImportance {
        // Importance is primarily driven by significance outcome
        return when (significance.outcome) {
            SignificanceOutcome.CRITICAL -> SignalImportance.CRITICAL
            SignificanceOutcome.HIGH_SIGNIFICANCE -> SignalImportance.HIGH
            SignificanceOutcome.SIGNIFICANT -> {
                if (source.authority >= SourceAuthority.NATIONAL) SignalImportance.MODERATE
                else SignalImportance.LOW
            }
            SignificanceOutcome.NOT_SIGNIFICANT -> SignalImportance.LOW
        }
    }

    private fun mapSourceTypeToCategory(type: IntelligenceSourceType): SignalCategory {
        return when (type) {
            IntelligenceSourceType.FOOD_SAFETY -> SignalCategory.FOOD
            IntelligenceSourceType.PRODUCT_RECALL -> SignalCategory.CONSUMER_PRODUCTS
            IntelligenceSourceType.ENVIRONMENTAL -> SignalCategory.ENVIRONMENT
            IntelligenceSourceType.SCREENING_GUIDELINE -> SignalCategory.SCREENING
            IntelligenceSourceType.REGULATORY, IntelligenceSourceType.GOVERNMENT -> SignalCategory.REGULATION
            IntelligenceSourceType.PUBLIC_HEALTH -> SignalCategory.PREVENTION
            IntelligenceSourceType.SCIENTIFIC -> SignalCategory.RESEARCH
            IntelligenceSourceType.CANCER_ORGANIZATION -> SignalCategory.RESEARCH
        }
    }
    
    private operator fun SourceAuthority.compareTo(other: SourceAuthority): Int {
        return this.ordinal.compareTo(other.ordinal)
    }
}
