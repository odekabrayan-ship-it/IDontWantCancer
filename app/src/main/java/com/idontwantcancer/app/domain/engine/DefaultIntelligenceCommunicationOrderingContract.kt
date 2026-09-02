package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.AttentionLevel
import com.idontwantcancer.app.domain.model.IntelligenceCommunicationHandoff
import com.idontwantcancer.app.domain.model.SignificanceOutcome
import javax.inject.Inject

/**
 * Default implementation of the ordering contract that uses deterministic
 * hierarchy of priority, significance, and stable identifiers.
 */
class DefaultIntelligenceCommunicationOrderingContract @Inject constructor() : 
    IntelligenceCommunicationOrderingContract {

    override val handoffComparator: Comparator<IntelligenceCommunicationHandoff> = 
        Comparator { a, b ->
            val pkgA = a.communicationPackage
            val pkgB = b.communicationPackage
            
            // 1. Existing Priority (AttentionLevel) - Lower ordinal is higher priority
            val priorityA = pkgA.priority ?: AttentionLevel.ROUTINE
            val priorityB = pkgB.priority ?: AttentionLevel.ROUTINE
            val priorityCompare = priorityA.ordinal.compareTo(priorityB.ordinal)
            if (priorityCompare != 0) return@Comparator priorityCompare
            
            // 2. Significance (SignificanceOutcome) - Higher ordinal is more significant
            val sigA = pkgA.significanceLevel ?: SignificanceOutcome.SIGNIFICANT
            val sigB = pkgB.significanceLevel ?: SignificanceOutcome.SIGNIFICANT
            val sigCompare = sigB.ordinal.compareTo(sigA.ordinal) // Descending
            if (sigCompare != 0) return@Comparator sigCompare
            
            // 3. Recency (Signal DetectedAt) - Newer first
            val timeA = pkgA.provenance.signal.detectedAt
            val timeB = pkgB.provenance.signal.detectedAt
            val timeCompare = timeB.compareTo(timeA) // Descending
            if (timeCompare != 0) return@Comparator timeCompare
            
            // 4. Identity - Alphanumeric stable tie-breaker
            a.intelligenceId.compareTo(b.intelligenceId)
        }
}
