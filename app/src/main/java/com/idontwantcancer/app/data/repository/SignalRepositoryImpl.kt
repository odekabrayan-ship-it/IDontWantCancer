package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.data.datasource.SignalDataSource
import com.idontwantcancer.app.data.local.dao.SignalDao
import com.idontwantcancer.app.data.local.mapper.toDomain as toDomainFromEntity
import com.idontwantcancer.app.data.local.mapper.toEntity
import com.idontwantcancer.app.data.mapper.toDomain as toDomainFromDto
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.model.SignalCategory
import com.idontwantcancer.app.domain.repository.SignalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of [SignalRepository] that serves as the bridge between
 * the domain layer and various intelligence data sources, including local memory.
 */
class SignalRepositoryImpl @Inject constructor(
    private val signalDataSource: SignalDataSource,
    private val signalDao: SignalDao,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : SignalRepository {

    override suspend fun getLatestSignals(): List<Signal> {
        return try {
            val remoteSignals = withContext(dispatcherProvider.default) {
                signalDataSource.getLatestSignals().map { it.toDomainFromDto() }
            }
            val entities = withContext(dispatcherProvider.default) {
                remoteSignals.map { it.toEntity() }
            }
            // Save to local memory in batch (Step 214)
            signalDao.upsertAll(entities)
            remoteSignals
        } catch (e: Exception) {
            // Offline-first fallback
            withContext(dispatcherProvider.default) {
                signalDao.getAllFlow().first().map { it.toDomainFromEntity() }
            }
        }
    }

    override suspend fun getAttentionSignals(): List<Signal> {
        return try {
            val remoteSignals = withContext(dispatcherProvider.default) {
                signalDataSource.getAttentionSignals().map { it.toDomainFromDto() }
            }
            val entities = withContext(dispatcherProvider.default) {
                remoteSignals.map { it.toEntity() }
            }
            signalDao.upsertAll(entities)
            remoteSignals
        } catch (e: Exception) {
            // Filter local memory for high importance
            withContext(dispatcherProvider.default) {
                signalDao.getAllFlow().first()
                    .map { it.toDomainFromEntity() }
                    .filter { it.importance.ordinal >= 2 } // HIGH or CRITICAL
            }
        }
    }

    override suspend fun searchSignals(query: String): List<Signal> = withContext(dispatcherProvider.io) {
        signalDao.search(query).map { it.toDomainFromEntity() }
    }

    override fun getSignalsByCategories(categories: List<SignalCategory>): Flow<List<Signal>> {
        return signalDao.getByCategoriesFlow(categories).map { entities ->
            entities.map { it.toDomainFromEntity() }
        }
    }

    override suspend fun getSignalById(signalId: String): Signal? = withContext(dispatcherProvider.io) {
        signalDao.getById(signalId)?.toDomainFromEntity()
    }

    override suspend fun updateActionTakenStatus(signalId: String, isTaken: Boolean) = withContext(dispatcherProvider.io) {
        signalDao.updateActionTakenStatus(signalId, isTaken)
    }
}
