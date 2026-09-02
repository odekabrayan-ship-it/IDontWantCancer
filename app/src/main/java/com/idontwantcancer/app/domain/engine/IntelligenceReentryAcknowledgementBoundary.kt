package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryAcknowledgementResult
import com.idontwantcancer.app.domain.model.IntelligenceReentryConsumerAcknowledgement

/**
 * Interface for the intelligence component responsible for receiving 
 * and recording consumer acknowledgements without granting authority over lifecycle.
 */
interface IntelligenceReentryAcknowledgementBoundary {
    /**
     * Records an acknowledgement from a consumer.
     *
     * @param acknowledgement The acknowledgement to record.
     * @return The structured acknowledgement result.
     */
    fun acknowledge(
        acknowledgement: IntelligenceReentryConsumerAcknowledgement
    ): IntelligenceReentryAcknowledgementResult
}
