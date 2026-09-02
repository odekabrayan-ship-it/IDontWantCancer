package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceBriefingContextResult
import com.idontwantcancer.app.domain.model.IntelligenceCommunicationPackage

/**
 * Interface for the intelligence component responsible for determining 
 * the minimum structured context required to understand an intelligence item.
 */
interface IntelligenceBriefingContextEngine {
    /**
     * Identifies the minimum necessary context for a specific communication package.
     *
     * @param pkg The communication package being evaluated.
     * @return The determined minimum context.
     */
    fun determineContext(
        pkg: IntelligenceCommunicationPackage
    ): IntelligenceBriefingContextResult
}
