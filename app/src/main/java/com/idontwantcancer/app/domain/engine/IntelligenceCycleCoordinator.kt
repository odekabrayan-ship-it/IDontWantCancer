package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceCycleResult

/**
 * Interface for the component responsible for orchestrating the autonomous intelligence cycle.
 */
interface IntelligenceCycleCoordinator {
    /**
     * Executes one complete intelligence cycle.
     *
     * @return The result of the cycle, containing statistics and any briefing generated.
     */
    suspend fun runCycle(): IntelligenceCycleResult
}
