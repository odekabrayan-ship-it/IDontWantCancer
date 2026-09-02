package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceCommunicationPackage
import com.idontwantcancer.app.domain.model.IntelligenceUncertaintyPresentationModel

/**
 * Interface for the intelligence component responsible for preserving 
 * and structuring uncertainty for the communication layer.
 */
interface IntelligenceUncertaintyEngine {
    /**
     * Determines the structured uncertainty presentation model for an intelligence item.
     *
     * @param pkg The communication package containing the intelligence context.
     * @return The determined uncertainty presentation model.
     */
    fun determineUncertainty(
        pkg: IntelligenceCommunicationPackage
    ): IntelligenceUncertaintyPresentationModel
}
