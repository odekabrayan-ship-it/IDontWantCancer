package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.repository.SignalRepository
import javax.inject.Inject

/**
 * Use case to search the available cancer-intelligence signals for a specific query.
 */
class SearchSignalsUseCase @Inject constructor(
    private val signalRepository: SignalRepository
) {
    /**
     * Searches for signals matching the provided query.
     *
     * @param query The search term.
     * @return A list of matching signals.
     */
    suspend operator fun invoke(query: String): List<Signal> {
        if (query.isBlank()) return emptyList()
        return signalRepository.searchSignals(query)
    }
}
