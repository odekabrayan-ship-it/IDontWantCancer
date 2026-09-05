package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.repository.PreventionRepository
import javax.inject.Inject

class ToggleActionAdoptionUseCase @Inject constructor(
    private val preventionRepository: PreventionRepository
) {
    suspend operator fun invoke(id: String) {
        preventionRepository.toggleActionAdoption(id)
    }
}
