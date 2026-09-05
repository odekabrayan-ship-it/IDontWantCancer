package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.data.local.dao.TreatmentManualDao
import com.idontwantcancer.app.data.local.mapper.toDomain
import com.idontwantcancer.app.data.local.mapper.toEntity
import com.idontwantcancer.app.domain.model.TreatmentManual
import com.idontwantcancer.app.domain.repository.HealingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealingRepositoryImpl @Inject constructor(
    private val treatmentManualDao: TreatmentManualDao,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : HealingRepository {

    override fun getTreatmentManuals(): Flow<List<TreatmentManual>> {
        return treatmentManualDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveTreatmentManuals(items: List<TreatmentManual>) = withContext(dispatcherProvider.io) {
        treatmentManualDao.upsertAll(items.map { it.toEntity() })
    }
}
