package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a short, foundational education module for cancer prevention.
 */
@Serializable
data class EducationLesson(
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val keyTakeaway: String,
    val source: String,
    val sourceUrl: String? = null
)
