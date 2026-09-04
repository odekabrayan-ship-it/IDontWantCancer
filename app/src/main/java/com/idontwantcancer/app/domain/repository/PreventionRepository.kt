package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.NutritionIntelligence
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
}
