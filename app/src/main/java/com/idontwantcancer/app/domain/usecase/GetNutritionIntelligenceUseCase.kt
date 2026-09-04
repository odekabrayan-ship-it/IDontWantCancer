package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.NutritionIntelligence
import com.idontwantcancer.app.domain.repository.PreventionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNutritionIntelligenceUseCase @Inject constructor(
    private val preventionRepository: PreventionRepository
) {
    operator fun invoke(): Flow<List<NutritionIntelligence>> {
        return preventionRepository.getNutritionIntelligence()
    }
}
