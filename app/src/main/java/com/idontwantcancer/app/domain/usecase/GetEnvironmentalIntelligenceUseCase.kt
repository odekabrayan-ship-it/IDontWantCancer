package com.idontwantcancer.app.domain.usecase

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.model.SignalCategory
import com.idontwantcancer.app.domain.repository.SignalRepository
import com.idontwantcancer.app.domain.repository.UserContextRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case to retrieve intelligence signals related to the user's environment and occupation.
 */
class GetEnvironmentalIntelligenceUseCase @Inject constructor(
    private val signalRepository: SignalRepository,
    private val userContextRepository: UserContextRepository
) {
    operator fun invoke(): Flow<List<Signal>> {
        val userCountry = userContextRepository.getUserCountryCode()
        val categories = listOf(SignalCategory.ENVIRONMENT, SignalCategory.OCCUPATIONAL)
        
        return signalRepository.getSignalsByCategories(categories).map { signals ->
            signals.filter { signal ->
                // Apply same geographical shield logic as briefing
                when (signal.scope) {
                    com.idontwantcancer.app.domain.model.GeographicScope.GLOBAL -> true
                    com.idontwantcancer.app.domain.model.GeographicScope.NATIONAL -> signal.targetCountryCode == userCountry
                    com.idontwantcancer.app.domain.model.GeographicScope.REGIONAL -> {
                        // For simplicity, treating regional as global for now in this pillar, 
                        // or could expand EU logic if needed.
                        true 
                    }
                }
            }
        }
    }
}
