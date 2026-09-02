package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import javax.inject.Inject

/**
 * Default implementation of the acknowledgement boundary.
 * Provides a deterministic registration point for consumer responses.
 */
class DefaultIntelligenceReentryAcknowledgementBoundary @Inject constructor() : 
    IntelligenceReentryAcknowledgementBoundary {

    override fun acknowledge(
        acknowledgement: IntelligenceReentryConsumerAcknowledgement
    ): IntelligenceReentryAcknowledgementResult {
        // Deterministic response without lifecycle mutation authority.
        // This established downstream processing status without changing domain state.
        return IntelligenceReentryAcknowledgementResult(
            acknowledgement = acknowledgement,
            isRegistered = true,
            reason = "Acknowledgement for ${acknowledgement.reentryIdentity} by ${acknowledgement.consumer} registered."
        )
    }
}
