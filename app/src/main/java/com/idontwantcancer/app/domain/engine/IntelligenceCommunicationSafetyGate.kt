package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.CommunicationReadinessResult
import com.idontwantcancer.app.domain.model.IntelligenceCommunicationPackage

/**
 * Interface for the intelligence component responsible for determining 
 * the structural safety and eligibility of intelligence for ordinary-person communication.
 */
interface IntelligenceCommunicationSafetyGate {
    /**
     * Evaluates whether a structured communication package is safe and eligible
     * to enter the communication pipeline.
     *
     * @param pkg The communication package being evaluated.
     * @return The structured readiness and safety result.
     */
    fun evaluateSafety(pkg: IntelligenceCommunicationPackage): CommunicationReadinessResult
}
