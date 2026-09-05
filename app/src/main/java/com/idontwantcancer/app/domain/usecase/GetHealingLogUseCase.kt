package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.HealingLogEntry
import com.idontwantcancer.app.domain.repository.HealingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHealingLogUseCase @Inject constructor(
    private val healingRepository: HealingRepository
) {
    operator fun invoke(): Flow<List<HealingLogEntry>> {
        return healingRepository.getHealingLog()
    }
}
