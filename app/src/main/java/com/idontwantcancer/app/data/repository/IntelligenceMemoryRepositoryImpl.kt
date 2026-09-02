package com.idontwantcancer.app.data.repository

import androidx.room.withTransaction
import com.idontwantcancer.app.data.local.AppDatabase
import com.idontwantcancer.app.data.local.dao.*
import com.idontwantcancer.app.data.local.mapper.toDomain
import com.idontwantcancer.app.data.local.mapper.toEntity
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Concrete implementation of [IntelligenceMemoryRepository] using Room DAOs.
 */
class IntelligenceMemoryRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val signalDao: SignalDao,
    private val sourceMaterialDao: SourceMaterialDao,
    private val threadDao: ThreadDao,
    private val conflictDao: ConflictDao,
    private val consolidatedEventDao: ConsolidatedEventDao,
    private val timelineEntryDao: TimelineEntryDao,
    private val supersessionRelationDao: SupersessionRelationDao,
    private val stateTransitionDao: StateTransitionDao,
    private val reentryLifecycleDao: ReentryLifecycleDao,
    private val reentryAuditDao: ReentryAuditDao,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : IntelligenceMemoryRepository {

    override suspend fun getPreviousMaterial(contentId: String): SourceMaterial? = withContext(dispatcherProvider.io) {
        sourceMaterialDao.getById(contentId)?.toDomain()
    }

    override suspend fun getSourceMaterialById(contentId: String): SourceMaterial? = withContext(dispatcherProvider.io) {
        sourceMaterialDao.getById(contentId)?.toDomain()
    }

    override suspend fun saveSourceMaterial(material: SourceMaterial) = withContext(dispatcherProvider.io) {
        val existing = sourceMaterialDao.getById(material.contentId)
        sourceMaterialDao.upsert(material.toEntity(firstObservedAt = existing?.firstObservedAt ?: System.currentTimeMillis()))
    }

    override suspend fun saveSignal(signal: Signal) = withContext(dispatcherProvider.io) {
        val existing = signalDao.getById(signal.id)
        signalDao.upsert(signal.toEntity(firstObservedAt = existing?.firstObservedAt ?: System.currentTimeMillis()))
    }

    override suspend fun getAllSignals(): List<Signal> = withContext(dispatcherProvider.default) {
        signalDao.getAllFlow().first().map { it.toDomain() }
    }

    override suspend fun getSignalById(id: String): Signal? = withContext(dispatcherProvider.io) {
        signalDao.getById(id)?.toDomain()
    }

    override suspend fun findThreadByTopic(topicIdentifier: String): IntelligenceThread? = withContext(dispatcherProvider.io) {
        threadDao.getByTopic(topicIdentifier)?.toDomain()
    }

    override suspend fun getThreadById(id: String): IntelligenceThread? = withContext(dispatcherProvider.io) {
        threadDao.getById(id)?.toDomain()
    }

    override suspend fun saveThread(thread: IntelligenceThread) = withContext(dispatcherProvider.io) {
        threadDao.upsert(thread.toEntity())
    }

    override suspend fun getAllConsolidatedEvents(): List<ConsolidatedEvent> = withContext(dispatcherProvider.default) {
        consolidatedEventDao.getAll().map { it.toDomain() }
    }

    override suspend fun getConsolidatedEventById(id: String): ConsolidatedEvent? = withContext(dispatcherProvider.io) {
        consolidatedEventDao.getById(id)?.toDomain()
    }

    override suspend fun saveConsolidatedEvent(event: ConsolidatedEvent) = withContext(dispatcherProvider.io) {
        consolidatedEventDao.upsert(event.toEntity())
    }

    override suspend fun saveTimelineEntry(entry: TimelineEntry) = withContext(dispatcherProvider.io) {
        timelineEntryDao.upsert(entry.toEntity())
    }

    override suspend fun getTimelineEntriesForThread(threadId: String): List<TimelineEntry> = withContext(dispatcherProvider.default) {
        timelineEntryDao.getByThreadId(threadId).map { it.toDomain() }
    }

    override suspend fun saveSupersessionRelation(relation: SupersessionRelation) = withContext(dispatcherProvider.io) {
        supersessionRelationDao.upsert(relation.toEntity())
    }

    override suspend fun getSupersessionRelationsForEntry(entryId: String): List<SupersessionRelation> = withContext(dispatcherProvider.default) {
        val asPrevious = supersessionRelationDao.getByPreviousId(entryId)
        val asSuperseding = supersessionRelationDao.getBySupersedingId(entryId)
        (asPrevious + asSuperseding).map { it.toDomain() }.distinctBy { it.id }
    }

    override suspend fun getConflictsForTopic(topicId: String): List<IntelligenceConflict> = withContext(dispatcherProvider.default) {
        conflictDao.getByTopic(topicId).map { it.toDomain() }
    }

    override suspend fun saveStateTransition(transition: IntelligenceStateTransition) = withContext(dispatcherProvider.io) {
        stateTransitionDao.upsert(transition.toEntity())
    }

    override suspend fun getStateTransitionsForThread(threadId: String): List<IntelligenceStateTransition> = withContext(dispatcherProvider.default) {
        stateTransitionDao.getByThreadId(threadId).map { it.toDomain() }
    }

    override suspend fun saveReentryLifecycle(lifecycle: IntelligenceReentryLifecycle) = withContext(dispatcherProvider.io) {
        reentryLifecycleDao.upsert(lifecycle.toEntity())
    }

    override suspend fun getReentryLifecycleByIdentity(identity: String): IntelligenceReentryLifecycle? = withContext(dispatcherProvider.io) {
        reentryLifecycleDao.getByIdentity(identity)?.toDomain()
    }

    override suspend fun recordReentryTransition(
        lifecycle: IntelligenceReentryLifecycle,
        auditEntry: IntelligenceReentryAuditEntry
    ) = withContext(dispatcherProvider.io) {
        database.withTransaction {
            reentryLifecycleDao.upsert(lifecycle.toEntity())
            reentryAuditDao.insert(auditEntry.toEntity())
        }
    }

    override suspend fun getReentryAuditHistory(identity: String): List<IntelligenceReentryAuditEntry> = withContext(dispatcherProvider.default) {
        reentryAuditDao.getByReentryIdentity(identity).map { it.toDomain() }
    }
}
