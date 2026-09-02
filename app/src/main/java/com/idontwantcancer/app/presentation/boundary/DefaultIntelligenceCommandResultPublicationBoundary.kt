package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryCompletionPublicationBoundary
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.presentation.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of the command publication boundary.
 * Delegates verified re-entry results to Step 101 domain authority.
 */
@Singleton
class DefaultIntelligenceCommandResultPublicationBoundary @Inject constructor(
    private val reentryPublicationBoundary: IntelligenceReentryCompletionPublicationBoundary,
    private val consumptionHandoverBridge: IntelligenceCommandPublicationConsumptionHandoverBoundary
) : IntelligenceCommandResultPublicationBoundary {

    private val _consumptionStream = MutableSharedFlow<IntelligenceCommandConsumptionRequest>(replay = 10)
    override val consumptionStream: Flow<IntelligenceCommandConsumptionRequest> = _consumptionStream.asSharedFlow()

    override suspend fun publishResult(
        request: IntelligenceCommandPublicationRequest
    ): IntelligenceCommandPublicationResult {
        val result = request.result
        val verification = request.verificationResult
        val now = Instant.now()
        val operationId = result.operationId ?: "anonymous"

        // 1. Verification Gate (Step 144 / 158 / 159 Requirement)
        if (verification.status != CommandVerificationStatus.VERIFIED) {
            return IntelligenceCommandPublicationResult(
                operationId = operationId,
                status = CommandPublicationStatus.SUPPRESSED,
                reason = "Publication suppressed: Result verification failed (${verification.status}).",
                publishedAt = now
            )
        }

        // 2. Identify Re-entry Result (Step 101 Integration)
        if (operationId.contains("::")) {
            publishToReentryPipeline(operationId, result, now)
        }

        val publishedResult = IntelligenceCommandPublicationResult(
            operationId = operationId,
            status = CommandPublicationStatus.PUBLISHED,
            reason = "Result successfully entered authoritative publication pipeline.",
            publishedAt = now
        )
        
        // Step 160 / 175 / 190: Formalize handover to consumption stream
        val handoverRequest = IntelligenceCommandPublicationConsumptionHandoverRequest(
            result = result,
            publicationResult = publishedResult
        )
        
        val consumptionRequest = consumptionHandoverBridge.routeToConsumption(handoverRequest)
        
        _consumptionStream.emit(consumptionRequest)

        return publishedResult
    }

    private suspend fun publishToReentryPipeline(
        identity: String,
        result: IntelligenceApplicationCommandResult,
        now: Instant
    ) {
        // Deterministic Adapter (Step 144 requirement)
        // Map presentation result back to Step 101 domain model
        val completionResult = IntelligenceReentryTransitionCompletionResult(
            reentryIdentity = identity,
            status = when (result) {
                is IntelligenceApplicationCommandResult.Success -> ReentryTransitionCompletionStatus.COMPLETED
                else -> ReentryTransitionCompletionStatus.NOT_COMPLETED
            },
            verificationResult = IntelligenceReentryPostTransitionVerificationResult(
                reentryIdentity = identity,
                status = if (result is IntelligenceApplicationCommandResult.Success) 
                            ReentryPostTransitionVerificationStatus.VERIFIED 
                         else ReentryPostTransitionVerificationStatus.INCONSISTENT,
                expectedState = null, // Unknown at this boundary
                actualState = null,
                verifiedAt = now
            ),
            completedAt = now
        )

        reentryPublicationBoundary.publishCompletion(completionResult)
    }
}
