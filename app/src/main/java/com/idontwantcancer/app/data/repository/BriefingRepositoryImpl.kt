package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.data.local.dao.BriefingSnapshotDao
import com.idontwantcancer.app.data.local.mapper.toDomain
import com.idontwantcancer.app.data.local.mapper.toEntity
import com.idontwantcancer.app.domain.engine.IntelligenceBriefingAssemblyEngine
import com.idontwantcancer.app.domain.engine.IntelligencePrioritizationEngine
import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import com.idontwantcancer.app.domain.repository.BriefingRepository
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

/**
 * Implementation of [BriefingRepository] that assembles the briefing 
 * from the agency's current memory of intelligence signals and manages snapshots.
 */
class BriefingRepositoryImpl @Inject constructor(
    private val memory: IntelligenceMemoryRepository,
    private val prioritizer: IntelligencePrioritizationEngine,
    private val assemblyEngine: IntelligenceBriefingAssemblyEngine,
    private val snapshotDao: BriefingSnapshotDao,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : BriefingRepository {

    override suspend fun getCurrentBriefing(): IntelligenceBriefing = withContext(dispatcherProvider.default) {
        val signals = memory.getAllSignals()
        val prioritized = prioritizer.prioritize(signals)
        assemblyEngine.assembleBriefing(prioritized, Instant.now())
    }

    override suspend fun saveBriefingSnapshot(briefing: IntelligenceBriefing) = withContext(dispatcherProvider.io) {
        snapshotDao.upsert(briefing.toEntity())
    }

    override suspend fun getBriefingSnapshotById(id: String): IntelligenceBriefing? = withContext(dispatcherProvider.io) {
        snapshotDao.getById(id)?.toDomain()
    }

    override suspend fun getAllBriefingSnapshots(): List<IntelligenceBriefing> = withContext(dispatcherProvider.io) {
        snapshotDao.getAllSnapshots().map { it.toDomain() }
    }
}
