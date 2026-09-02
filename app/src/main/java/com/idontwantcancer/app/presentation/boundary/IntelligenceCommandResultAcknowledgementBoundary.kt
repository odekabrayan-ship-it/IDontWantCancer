package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementResult

/**
 * Authoritative boundary for recording and recognizing that a downstream 
 * authority has received and accepted a handed-off command result.
 */
interface IntelligenceCommandResultAcknowledgementBoundary {
    /**
     * Records an acknowledgement for a consumed command result.
     *
     * @param request The formalized acknowledgement request from Step 161.
     * @return The structured acknowledgement result.
     */
    suspend fun acknowledge(
        request: IntelligenceCommandAcknowledgementRequest
    ): IntelligenceCommandAcknowledgementResult
}
