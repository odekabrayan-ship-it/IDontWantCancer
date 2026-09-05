package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.HealingLogEntry
import com.idontwantcancer.app.domain.model.HealingLogType
import com.idontwantcancer.app.domain.repository.HealingRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class LogHealingActionUseCase @Inject constructor(
    private val healingRepository: HealingRepository
) {
    suspend operator fun invoke(directiveId: String, directiveName: String, type: HealingLogType) {
        val entry = HealingLogEntry(
            id = UUID.randomUUID().toString(),
            directiveId = directiveId,
            directiveName = directiveName,
            timestamp = Instant.now(),
            type = type
        )
        healingRepository.logHealingAction(entry)
    }
}
