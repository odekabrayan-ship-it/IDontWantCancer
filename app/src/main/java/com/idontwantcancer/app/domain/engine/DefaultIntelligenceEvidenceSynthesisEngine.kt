package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceEvidenceSynthesisEngine] that uses 
 * deterministic rules to determine collective evidence support.
 */
class DefaultIntelligenceEvidenceSynthesisEngine @Inject constructor() : IntelligenceEvidenceSynthesisEngine {

    override fun synthesizeEvidence(
        threadId: String,
        signals: List<Signal>,
        conflicts: List<IntelligenceConflict>
    ): EvidenceSynthesisResult {
        val now = Instant.now()
        
        // 1. Check for Conflicts (Step 53 integration)
        val unresolvedConflicts = conflicts.filter { it.resolutionStatus == ResolutionStatus.UNRESOLVED }
        if (unresolvedConflicts.isNotEmpty()) {
            return EvidenceSynthesisResult(
                threadId = threadId,
                level = EvidenceSynthesisLevel.CONTESTED,
                contributingSignalIds = signals.map { it.id },
                conflictingSignalIds = unresolvedConflicts.flatMap { it.competingSignalIds }.distinct(),
                reason = "Collective evidence is contested due to unresolved contradictions between authoritative sources.",
                synthesizedAt = now
            )
        }

        if (signals.isEmpty()) {
            return EvidenceSynthesisResult(
                threadId = threadId,
                level = EvidenceSynthesisLevel.INSUFFICIENT,
                contributingSignalIds = emptyList(),
                conflictingSignalIds = emptyList(),
                reason = "No evidence signals have been identified for this topic.",
                synthesizedAt = now
            )
        }

        // 2. Evaluate Corroboration and Authority
        val independentSources = signals.flatMap { sig -> 
            listOf(sig.source.name) + sig.supportingSources.map { it.name }
        }.distinct()

        val maxConfidence = signals.maxOf { it.confidence }
        
        val level = when {
            // Strong Corroboration: Multiple independent sources with high confidence
            independentSources.size >= 3 && maxConfidence >= SignalConfidence.HIGH -> {
                EvidenceSynthesisLevel.STRONGLY_SUPPORTED
            }
            // Supported: One or more reliable sources with at least moderate confidence
            maxConfidence >= SignalConfidence.MODERATE -> {
                EvidenceSynthesisLevel.SUPPORTED
            }
            // Limited: Only low confidence evidence available
            else -> {
                EvidenceSynthesisLevel.LIMITED
            }
        }

        return EvidenceSynthesisResult(
            threadId = threadId,
            level = level,
            contributingSignalIds = signals.map { it.id },
            conflictingSignalIds = emptyList(),
            reason = "Evidence synthesis established as ${level.name} based on ${independentSources.size} independent sources and ${maxConfidence} max confidence.",
            synthesizedAt = now
        )
    }
}
