package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.repository.SignalRepository
import javax.inject.Inject

class UpdateWatchStatusUseCase @Inject constructor(
    private val signalRepository: SignalRepository
) {
    suspend operator fun invoke(signalId: String, isWatched: Boolean) {
        signalRepository.updateWatchStatus(signalId, isWatched)
    }
}
