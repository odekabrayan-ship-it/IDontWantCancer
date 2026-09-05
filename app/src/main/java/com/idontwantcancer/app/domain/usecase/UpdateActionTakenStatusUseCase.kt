package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.repository.SignalRepository
import javax.inject.Inject

class UpdateActionTakenStatusUseCase @Inject constructor(
    private val signalRepository: SignalRepository
) {
    suspend operator fun invoke(signalId: String, isTaken: Boolean) {
        signalRepository.updateActionTakenStatus(signalId, isTaken)
    }
}
