package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.data.local.dao.*
import com.idontwantcancer.app.data.local.mapper.*
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.HealingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealingRepositoryImpl @Inject constructor(
    private val treatmentManualDao: TreatmentManualDao,
    private val symptomDirectiveDao: SymptomDirectiveDao,
    private val patientTruthCheckDao: PatientTruthCheckDao,
    private val healingLogDao: HealingLogDao,
    private val redFlagDirectiveDao: RedFlagDirectiveDao,
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

    override fun getSymptomDirectives(): Flow<List<SymptomDirective>> {
        return symptomDirectiveDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveSymptomDirectives(items: List<SymptomDirective>) = withContext(dispatcherProvider.io) {
        symptomDirectiveDao.upsertAll(items.map { it.toEntity() })
    }

    override fun getPatientTruthChecks(): Flow<List<PatientTruthCheck>> {
        return patientTruthCheckDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun savePatientTruthChecks(items: List<PatientTruthCheck>) = withContext(dispatcherProvider.io) {
        patientTruthCheckDao.upsertAll(items.map { it.toEntity() })
    }

    override fun getRedFlagDirectives(): Flow<List<RedFlagDirective>> {
        return redFlagDirectiveDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveRedFlagDirectives(items: List<RedFlagDirective>) = withContext(dispatcherProvider.io) {
        redFlagDirectiveDao.upsertAll(items.map { it.toEntity() })
    }

    override fun getHealingLog(): Flow<List<HealingLogEntry>> {
        return healingLogDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun logHealingAction(entry: HealingLogEntry) = withContext(dispatcherProvider.io) {
        healingLogDao.insert(entry.toEntity())
    }
}
