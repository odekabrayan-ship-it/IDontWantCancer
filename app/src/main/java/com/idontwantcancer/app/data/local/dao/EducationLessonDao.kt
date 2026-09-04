package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.EducationLessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationLessonDao {
    @Query("SELECT * FROM education_lessons")
    fun getAllFlow(): Flow<List<EducationLessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<EducationLessonEntity>)

    @Query("DELETE FROM education_lessons")
    suspend fun deleteAll()
}
