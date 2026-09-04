package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.NutritionIntelligenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionIntelligenceDao {
    @Query("SELECT * FROM nutrition_intelligence")
    fun getAllFlow(): Flow<List<NutritionIntelligenceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<NutritionIntelligenceEntity>)

    @Query("DELETE FROM nutrition_intelligence")
    suspend fun deleteAll()
}
