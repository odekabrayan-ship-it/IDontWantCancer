package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.EducationLesson
import com.idontwantcancer.app.domain.repository.PreventionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEducationLessonsUseCase @Inject constructor(
    private val preventionRepository: PreventionRepository
) {
    operator fun invoke(): Flow<List<EducationLesson>> {
        return preventionRepository.getEducationLessons()
    }
}
