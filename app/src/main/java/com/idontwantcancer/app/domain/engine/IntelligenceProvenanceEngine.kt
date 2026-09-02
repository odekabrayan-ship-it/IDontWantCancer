package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceBriefingItem
import com.idontwantcancer.app.domain.model.IntelligenceProvenance

/**
 * Interface for the intelligence component responsible for reconstructing 
 * the provenance and audit trail of intelligence items.
 */
interface IntelligenceProvenanceEngine {
    /**
     * Reconstructs the full provenance chain for a specific signal.
     *
     * @param signalId The unique identifier of the signal.
     * @return The reconstructed provenance record.
     */
    suspend fun getSignalProvenance(signalId: String): IntelligenceProvenance

    /**
     * Reconstructs provenance starting from a briefing item.
     *
     * @param item The briefing item to trace.
     * @return The reconstructed provenance record.
     */
    suspend fun getBriefingProvenance(item: IntelligenceBriefingItem): IntelligenceProvenance
}
