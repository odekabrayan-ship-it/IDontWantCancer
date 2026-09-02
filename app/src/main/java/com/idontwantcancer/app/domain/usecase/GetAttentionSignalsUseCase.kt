package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.repository.SignalRepository
import javax.inject.Inject

/**
 * Use case to retrieve intelligence signals that require immediate user attention.
 */
class GetAttentionSignalsUseCase @Inject constructor(
    private val signalRepository: SignalRepository
) {
    suspend operator fun invoke(): List<Signal> {
        return signalRepository.getAttentionSignals()
    }
}
