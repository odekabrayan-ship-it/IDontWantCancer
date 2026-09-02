package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.data.datasource.SignalDataSource
import com.idontwantcancer.app.data.local.dao.SignalDao
import com.idontwantcancer.app.data.local.mapper.toDomain
import com.idontwantcancer.app.data.local.mapper.toEntity
import com.idontwantcancer.app.data.mapper.toDomain
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.repository.SignalRepository
import kotlinx.coroutines.flow.first
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
                signalDataSource.getLatestSignals().map { it.toDomain() }
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
                signalDao.getAllFlow().first().map { it.toDomain() }
            }
        }
    }

    override suspend fun getAttentionSignals(): List<Signal> {
        return try {
            val remoteSignals = withContext(dispatcherProvider.default) {
                signalDataSource.getAttentionSignals().map { it.toDomain() }
            }
            val entities = withContext(dispatcherProvider.default) {
                remoteSignals.map { it.toEntity() }
            }
            // Save to local memory in batch (Step 214)
            signalDao.upsertAll(entities)
            remoteSignals
        } catch (e: Exception) {
            // Fallback to local memory (filtering by significance conceptually)
            withContext(dispatcherProvider.default) {
                signalDao.getAllFlow().first().map { it.toDomain() }
            }
        }
    }

    override suspend fun searchSignals(query: String): List<Signal> {
        return try {
            withContext(dispatcherProvider.default) {
                signalDataSource.searchSignals(query).map { it.toDomain() }
            }
        } catch (e: Exception) {
            // Search local memory if remote fails
            withContext(dispatcherProvider.default) {
                signalDao.getAllFlow().first()
                    .map { it.toDomain() }
                    .filter { it.title.contains(query, ignoreCase = true) || it.summary.contains(query, ignoreCase = true) }
            }
        }
    }

    override suspend fun getSignalById(signalId: String): Signal? {
        // Try local memory first for stable identification
        val localSignal = withContext(dispatcherProvider.default) {
            signalDao.getById(signalId)?.toDomain()
        }
        if (localSignal != null) return localSignal

        return try {
            val remoteSignal = withContext(dispatcherProvider.default) {
                signalDataSource.getSignalById(signalId).toDomain()
            }
            signalDao.upsert(remoteSignal.toEntity())
            remoteSignal
        } catch (e: Exception) {
            null
        }
    }
}
