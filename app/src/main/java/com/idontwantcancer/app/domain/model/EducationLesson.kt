package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a 60-second masterclass in cancer intelligence.
 */
@Serializable
data class EducationLesson(
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val keyTakeaway: String,
    val source: String,
    val sourceUrl: String? = null,
    val isRead: Boolean = false
)
