package com.idontwantcancer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "education_lessons")
data class EducationLessonEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val keyTakeaway: String,
    val source: String,
    val sourceUrl: String?
)
