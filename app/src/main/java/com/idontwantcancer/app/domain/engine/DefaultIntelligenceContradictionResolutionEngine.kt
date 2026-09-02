package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceContradictionResolutionEngine] that uses 
 * deterministic rules to classify and resolve intelligence disagreements.
 */
class DefaultIntelligenceContradictionResolutionEngine @Inject constructor() : IntelligenceContradictionResolutionEngine {

    override suspend fun analyzeContradictions(
        assessment: EvidenceAssessment,
        existingSignals: List<Signal>,
        relations: List<SupersessionRelation>
    ): List<IntelligenceConflict> {
        val conflicts = mutableListOf<IntelligenceConflict>()
        val now = Instant.now()
        
        for (signal in existingSignals) {
            if (signal.source.name == assessment.change.sourceId) continue

            if (signal.title.equals(assessment.change.description, ignoreCase = true)) {
                
                // 1. Check for formal Supersession or Correction
                val relation = relations.find { 
                    (it.previousEntryId == signal.id && it.supersedingEntryId == assessment.change.id) ||
                    (it.previousEntryId == assessment.change.id && it.supersedingEntryId == signal.id)
                }

                if (relation != null) {
                    conflicts.add(
                        IntelligenceConflict(
                            id = UUID.randomUUID().toString(),
                            topicIdentifier = signal.title,
                            participatingSourceIds = listOf(signal.source.name, assessment.change.sourceId),
                            type = mapRelationToConflictType(relation.type),
                            resolutionStatus = ResolutionStatus.SUPERSEDED,
                            competingSignalIds = listOf(signal.id, assessment.change.id),
                            detectedAt = now,
                            resolutionReasoning = "Disagreement explained by official relationship: ${relation.type}"
                        )
                    )
                    continue
                }

                // 2. Check for Scope Difference
                val newCategory = assessment.change.categoryConcept()
                if (newCategory != null && signal.category != newCategory) {
                    conflicts.add(
                        IntelligenceConflict(
                            id = UUID.randomUUID().toString(),
                            topicIdentifier = signal.title,
                            participatingSourceIds = listOf(signal.source.name, assessment.change.sourceId),
                            type = ConflictType.SCOPE_DIFFERENCE,
                            resolutionStatus = ResolutionStatus.EXPLAINED_BY_CONTEXT,
                            competingSignalIds = listOf(signal.id, assessment.change.id),
                            detectedAt = now,
                            resolutionReasoning = "Apparent contradiction explained by different context scopes: ${signal.category} vs $newCategory"
                        )
                    )
                    continue
                }

                // 3. Fallback: Unresolved True Contradiction
                conflicts.add(
                    IntelligenceConflict(
                        id = UUID.randomUUID().toString(),
                        topicIdentifier = signal.title,
                        participatingSourceIds = listOf(signal.source.name, assessment.change.sourceId),
                        type = ConflictType.DIRECT_CONTRADICTION,
                        resolutionStatus = ResolutionStatus.UNRESOLVED,
                        competingSignalIds = listOf(signal.id, assessment.change.id),
                        detectedAt = now,
                        resolutionReasoning = "Competing authoritative claims for the same topic require investigation."
                    )
                )
            }
        }
        
        return conflicts
    }

    private fun mapRelationToConflictType(type: SupersessionType): ConflictType {
        return when (type) {
            SupersessionType.CORRECTION -> ConflictType.CORRECTION
            SupersessionType.REPLACEMENT, SupersessionType.RETRACTION -> ConflictType.SUPERSESSION
            else -> ConflictType.TEMPORAL_DIFFERENCE
        }
    }

    private fun DetectedChange.categoryConcept(): SignalCategory? {
        return null 
    }
}
