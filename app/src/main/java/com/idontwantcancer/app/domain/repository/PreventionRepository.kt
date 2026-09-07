package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.EducationLesson
import com.idontwantcancer.app.domain.model.NutritionIntelligence
import com.idontwantcancer.app.domain.model.PreventionAction
import kotlinx.coroutines.flow.Flow

/**
 * Repository for permanent foundational prevention intelligence.
 */
interface PreventionRepository {
    /**
     * Retrieves all nutritional intelligence items.
     */
    fun getNutritionIntelligence(): Flow<List<NutritionIntelligence>>

    /**
     * Saves foundational nutritional intelligence.
     */
    suspend fun saveNutritionIntelligence(items: List<NutritionIntelligence>)

    /**
     * Retrieves all education lessons.
     */
    fun getEducationLessons(): Flow<List<EducationLesson>>

    /**
     * Saves foundational education lessons.
     */
    suspend fun saveEducationLessons(items: List<EducationLesson>)

    /**
     * Retrieves all available prevention actions.
     */
    fun getPreventionActions(): Flow<List<PreventionAction>>

    /**
     * Updates the adoption status of a specific action.
     */
    suspend fun toggleActionAdoption(id: String)

    /**
     * Updates the read status of a specific lesson.
     */
    suspend fun updateLessonReadStatus(id: String, isRead: Boolean)

    /**
     * Saves foundational prevention actions.
     */
    suspend fun savePreventionActions(items: List<PreventionAction>)
}
