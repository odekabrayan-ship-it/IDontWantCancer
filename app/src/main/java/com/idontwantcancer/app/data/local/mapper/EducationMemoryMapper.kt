package com.idontwantcancer.app.data.local.mapper

import com.idontwantcancer.app.data.local.entity.EducationLessonEntity
import com.idontwantcancer.app.domain.model.EducationLesson

fun EducationLessonEntity.toDomain(): EducationLesson {
    return EducationLesson(
        id = id,
        title = title,
        summary = summary,
        content = content,
        keyTakeaway = keyTakeaway,
        source = source,
        sourceUrl = sourceUrl,
        isRead = isRead
    )
}

fun EducationLesson.toEntity(): EducationLessonEntity {
    return EducationLessonEntity(
        id = id,
        title = title,
        summary = summary,
        content = content,
        keyTakeaway = keyTakeaway,
        source = source,
        sourceUrl = sourceUrl,
        isRead = isRead
    )
}
