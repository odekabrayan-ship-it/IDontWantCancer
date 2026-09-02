package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.repository.SignalRepository

/**
 * Use case to retrieve the latest evaluated cancer-intelligence signals.
 */
class GetLatestSignalsUseCase(
    private val signalRepository: SignalRepository
) {
    suspend operator fun invoke(): List<Signal> {
        return signalRepository.getLatestSignals()
    }
}
