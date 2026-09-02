package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandIdempotencyStatus
import com.idontwantcancer.app.presentation.model.IntelligenceCommandIdempotencyResult
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the command idempotency boundary.
 * Maps command identities to existing authoritative semantics.
 */
class DefaultIntelligenceCommandIdempotencyBoundary @Inject constructor() : 
    IntelligenceCommandIdempotencyBoundary {

    override fun evaluateIdempotency(
        interaction: IntelligenceUiInteraction,
        commandIdentity: String
    ): IntelligenceCommandIdempotencyResult {
        val now = Instant.now()

        // 1. Identify Natural Idempotency
        // Simple UI interactions like "Clear Search" or "View Details" are 
        // naturally idempotent in the current architecture (last one wins).
        val status = when (interaction) {
            is IntelligenceUiInteraction.ClearSearch,
            is IntelligenceUiInteraction.ViewSignalDetails,
            is IntelligenceUiInteraction.NavigateBack -> {
                CommandIdempotencyStatus.PROCEED
            }
            
            // 2. Identify Domain Command Identity
            // If the identity string matches a pattern indicating an already-processed 
            // operation (e.g. from an existing result store), we would return REUSE_RESULT.
            // For now, we assume PROCEED and rely on established lower-level authorities 
            // (like Step 78 Deduplication or Step 80 Transition Guard) to enforce 
            // terminal idempotency.
            else -> CommandIdempotencyStatus.PROCEED
        }

        return IntelligenceCommandIdempotencyResult(
            commandIdentity = commandIdentity,
            status = status,
            reason = "Relying on existing authority for terminal idempotency.",
            evaluatedAt = now
        )
    }
}
