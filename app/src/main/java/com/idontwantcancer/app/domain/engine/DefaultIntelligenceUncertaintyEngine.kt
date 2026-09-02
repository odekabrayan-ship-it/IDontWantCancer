package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceUncertaintyEngine] that applies 
 * deterministic domain rules to identify and preserve structured uncertainty.
 */
class DefaultIntelligenceUncertaintyEngine @Inject constructor() : IntelligenceUncertaintyEngine {

    override fun determineUncertainty(
        pkg: IntelligenceCommunicationPackage
    ): IntelligenceUncertaintyPresentationModel {
        val synthesisLevel = pkg.evidenceSynthesis.level
        val confidenceLevel = pkg.confidence ?: SignalConfidence.LOW
        val gapLevel = pkg.evidenceGap.level
        val resolutionStatus = pkg.conflicts.firstOrNull()?.resolutionStatus ?: ResolutionStatus.RESOLVED

        // Deterministic flags based on existing domain states
        val isContested = synthesisLevel == EvidenceSynthesisLevel.CONTESTED || 
                         resolutionStatus == ResolutionStatus.UNRESOLVED ||
                         resolutionStatus == ResolutionStatus.INSUFFICIENT_INFORMATION

        val isDeveloping = confidenceLevel == SignalConfidence.LOW || 
                          synthesisLevel == EvidenceSynthesisLevel.LIMITED ||
                          synthesisLevel == EvidenceSynthesisLevel.INSUFFICIENT

        val isIncomplete = gapLevel == EvidenceGapLevel.INSUFFICIENT_SUPPORT ||
                          gapLevel == EvidenceGapLevel.MISSING_SCOPE_COVERAGE

        val requiresQualification = pkg.communicationReadiness.requiresQualification || 
                                   isContested || isDeveloping || isIncomplete

        return IntelligenceUncertaintyPresentationModel(
            intelligenceId = pkg.intelligenceId,
            synthesisLevel = synthesisLevel,
            confidenceLevel = confidenceLevel,
            gapLevel = gapLevel,
            resolutionStatus = resolutionStatus,
            requiresQualification = requiresQualification,
            isContested = isContested,
            isDeveloping = isDeveloping,
            isIncomplete = isIncomplete
        )
    }
}
