package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.IntelligenceBriefing

interface BriefingRepository {
    /**
     * Retrieves the currently available cancer-intelligence briefing.
     */
    suspend fun getCurrentBriefing(): IntelligenceBriefing

    /**
     * Saves a briefing snapshot to historical memory.
     */
    suspend fun saveBriefingSnapshot(briefing: IntelligenceBriefing)

    /**
     * Retrieves a historical briefing snapshot by its ID.
     */
    suspend fun getBriefingSnapshotById(id: String): IntelligenceBriefing?

    /**
     * Retrieves all historical briefing snapshots.
     */
    suspend fun getAllBriefingSnapshots(): List<IntelligenceBriefing>
}
