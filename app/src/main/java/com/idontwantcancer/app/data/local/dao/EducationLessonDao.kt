package com.idontwantcancer.app.data.local.dao

import androidx.room.*
import com.idontwantcancer.app.data.local.entity.EducationLessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationLessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<EducationLessonEntity>)

    @Query("SELECT * FROM education_lessons")
    fun getAllFlow(): Flow<List<EducationLessonEntity>>

    @Query("UPDATE education_lessons SET isRead = :isRead WHERE id = :id")
    suspend fun updateReadStatus(id: String, isRead: Boolean)

    @Query("DELETE FROM education_lessons")
    suspend fun deleteAll()
}
