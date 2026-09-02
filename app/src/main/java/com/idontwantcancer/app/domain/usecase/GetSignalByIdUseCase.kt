package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.repository.SignalRepository
import javax.inject.Inject

/**
 * Use case to retrieve a specific cancer-intelligence signal by its identifier.
 */
class GetSignalByIdUseCase @Inject constructor(
    private val signalRepository: SignalRepository
) {
    /**
     * Retrieves a signal by its ID.
     *
     * @param signalId The unique identifier of the signal.
     * @return The matching signal, or null if not found.
     */
    suspend operator fun invoke(signalId: String): Signal? {
        if (signalId.isBlank()) return null
        return signalRepository.getSignalById(signalId)
    }
}
