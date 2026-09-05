package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.data.local.dao.EducationLessonDao
import com.idontwantcancer.app.data.local.dao.NutritionIntelligenceDao
import com.idontwantcancer.app.data.local.dao.PreventionActionDao
import com.idontwantcancer.app.data.local.mapper.toDomain
import com.idontwantcancer.app.data.local.mapper.toEntity
import com.idontwantcancer.app.domain.model.EducationLesson
import com.idontwantcancer.app.domain.model.NutritionIntelligence
import com.idontwantcancer.app.domain.model.PreventionAction
import com.idontwantcancer.app.domain.repository.PreventionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreventionRepositoryImpl @Inject constructor(
    private val nutritionDao: NutritionIntelligenceDao,
    private val educationDao: EducationLessonDao,
    private val actionDao: PreventionActionDao,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : PreventionRepository {

    override fun getNutritionIntelligence(): Flow<List<NutritionIntelligence>> {
        return nutritionDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveNutritionIntelligence(items: List<NutritionIntelligence>) = withContext(dispatcherProvider.io) {
        nutritionDao.upsertAll(items.map { it.toEntity() })
    }

    override fun getEducationLessons(): Flow<List<EducationLesson>> {
        return educationDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveEducationLessons(items: List<EducationLesson>) = withContext(dispatcherProvider.io) {
        educationDao.upsertAll(items.map { it.toEntity() })
    }

    override fun getPreventionActions(): Flow<List<PreventionAction>> {
        return actionDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun toggleActionAdoption(id: String) = withContext(dispatcherProvider.io) {
        val existing = actionDao.getById(id)
        if (existing != null) {
            actionDao.updateAdoptionStatus(id, !existing.isAdopted)
        }
    }

    override suspend fun savePreventionActions(items: List<PreventionAction>) = withContext(dispatcherProvider.io) {
        actionDao.upsertAll(items.map { it.toEntity() })
    }
}
