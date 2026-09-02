package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceCommunicationIntegrityResult
import com.idontwantcancer.app.domain.model.IntelligenceCommunicationPackage

/**
 * Interface for the intelligence component responsible for verifying that
 * a communication package faithfully represents its underlying intelligence.
 */
interface IntelligenceCommunicationPackageIntegrityEngine {
    /**
     * Verifies the integrity of a communication package.
     *
     * @param pkg The package to verify.
     * @return The structured integrity result.
     */
    fun verifyPackageIntegrity(
        pkg: IntelligenceCommunicationPackage
    ): IntelligenceCommunicationIntegrityResult
}
