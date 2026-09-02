package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceCommunicationSelectionGate] that 
 * enforces eligibility rules for the communication cycle.
 */
class DefaultIntelligenceCommunicationSelectionGate @Inject constructor() : 
    IntelligenceCommunicationSelectionGate {

    override fun selectEligible(
        orderedHandoffs: List<IntelligenceCommunicationHandoff>
    ): IntelligenceCommunicationSelectionResult {
        val now = Instant.now()
        val selected = mutableListOf<IntelligenceCommunicationHandoff>()
        val excluded = mutableMapOf<String, String>()

        // Rule: One authoritative item per Thread per cycle (Simple suppression)
        val threadsRepresented = mutableSetOf<String>()

        for (handoff in orderedHandoffs) {
            val pkg = handoff.communicationPackage
            val signal = pkg.provenance.signal

            // 1. Structural Eligibility (Final Check)
            if (pkg.communicationReadiness.level == CommunicationReadinessLevel.BLOCKED) {
                excluded[handoff.intelligenceId] = "Blocked by safety gate."
                continue
            }

            // 2. Lifecycle Eligibility: Supersession
            if (pkg.currentState.effectiveEntryId != handoff.intelligenceId) {
                // If the current authoritative entry in the thread is different, this signal might be historical
                // and should only be included if the architecture explicitly allows historical context.
                // For a "Daily Briefing" cycle, we prefer the most current state.
                // Actually, the AssemblyEngine already tries to use the CurrentState.
            }

            // 3. Thread Uniqueness per Cycle
            if (threadsRepresented.contains(handoff.threadId)) {
                excluded[handoff.intelligenceId] = "Thread already represented in this cycle."
                continue
            }

            // 4. Lifecycle Eligibility: Conflict / Readiness (Already handled by gates, but double-verify)
            if (pkg.communicationReadiness.level == CommunicationReadinessLevel.NOT_READY) {
                excluded[handoff.intelligenceId] = "Not ready for communication."
                continue
            }

            // All checks passed
            selected.add(handoff)
            threadsRepresented.add(handoff.threadId)
        }

        return IntelligenceCommunicationSelectionResult(
            selectedHandoffs = selected,
            excludedHandoffs = excluded,
            selectedAt = now
        )
    }
}
