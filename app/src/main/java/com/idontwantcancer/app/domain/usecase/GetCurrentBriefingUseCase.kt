package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import com.idontwantcancer.app.domain.repository.BriefingRepository
import javax.inject.Inject

/**
 * Use case to retrieve the current cancer-intelligence briefing.
 */
class GetCurrentBriefingUseCase @Inject constructor(
    private val briefingRepository: BriefingRepository
) {
    suspend operator fun invoke(): IntelligenceBriefing {
        return briefingRepository.getCurrentBriefing()
    }
}
