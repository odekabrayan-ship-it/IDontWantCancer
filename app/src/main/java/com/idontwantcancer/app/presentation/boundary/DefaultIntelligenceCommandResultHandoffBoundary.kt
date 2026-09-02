package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationHandoffBoundary
import com.idontwantcancer.app.domain.model.ApplicationStateReconciliationStatus
import com.idontwantcancer.app.domain.model.IntelligenceReentryApplicationStateReconciliationResult
import com.idontwantcancer.app.presentation.model.*
import javax.inject.Inject

/**
 * Default implementation of the command result handoff boundary.
 * Routes results to authoritative destinations using deterministic adapters.
 */
class DefaultIntelligenceCommandResultHandoffBoundary @Inject constructor(
    private val reentryHandoffBoundary: IntelligenceReentryReconciliationHandoffBoundary,
    private val acceptanceBoundary: IntelligenceCommandResultAcceptanceBoundary,
    private val rejectionBoundary: IntelligenceCommandResultRejectionBoundary,
    private val dispositionBoundary: IntelligenceCommandResultDispositionBoundary,
    private val closureBoundary: IntelligenceCommandResultClosureBoundary,
    private val verificationBridge: IntelligenceCommandResultVerificationHandoverBoundary,
    private val publicationBridge: IntelligenceCommandVerificationPublicationHandoverBoundary,
    private val consumptionBridge: IntelligenceCommandPublicationConsumptionHandoverBoundary,
    private val acknowledgementHandoverBridge: IntelligenceCommandConsumptionAcknowledgementHandoverBoundary,
    private val closureHandoverBridge: IntelligenceCommandAcknowledgementClosureHandoverBoundary
) : IntelligenceCommandResultHandoffBoundary {

    override suspend fun performHandoff(result: IntelligenceApplicationCommandResult) {
        val operationId = result.operationId ?: return

        // 1. Step 143 / 158 / 173 / 188: Result Verification Bridge
        val verificationHandover = IntelligenceCommandResultVerificationHandoverRequest(result)
        val verification = verificationBridge.routeToVerification(verificationHandover)
        
        // 2. Step 144 / 159 / 174 / 189: Result Publication Bridge
        val publicationHandover = IntelligenceCommandVerificationPublicationHandoverRequest(result, verification)
        val publication = publicationBridge.routeToPublication(publicationHandover)

        // 3. Step 145 / 160 / 161 / 175 / 176 / 190 / 191: Consumption and Acknowledgement Bridge
        // Construct consumption request context for the acknowledgement bridge
        val pubHandoverRequest = IntelligenceCommandPublicationConsumptionHandoverRequest(result, publication)
        val consumptionRequest = consumptionBridge.routeToConsumption(pubHandoverRequest)
        val ackHandoverRequest = IntelligenceCommandConsumptionAcknowledgementHandoverRequest(result, consumptionRequest)
        val acknowledgement = acknowledgementHandoverBridge.routeToAcknowledgement(ackHandoverRequest)

        // 4. Destination: Re-entry Reconciliation (Step 104)
        if (operationId.contains("::")) {
            val domainResult = mapToDomainReconciliationResult(operationId, result)
            reentryHandoffBoundary.handoffResult(domainResult)
        }
        
        // 5. Step 125: Evaluate Acceptance by Destination
        val acceptance = acceptanceBoundary.evaluateAcceptance(result)

        // 6. Step 126: Record Rejection if applicable
        if (acceptance.status == CommandAcceptanceStatus.REJECTED) {
            rejectionBoundary.recordRejection(result, acceptance.reason ?: "Contract rejection.")
        }

        // 7. Step 127: Final Receiving-Side Disposition
        dispositionBoundary.evaluateDisposition(result)

        // 8. Step 128 / 147 / 162 / 177 / 192: Final Operational Closure Bridge
        val ackClosureHandover = IntelligenceCommandAcknowledgementClosureHandoverRequest(result, acknowledgement)
        closureHandoverBridge.routeToClosure(ackClosureHandover)
        
        // Destination: Audit (Step 81)
        // Handled by existing domain-level audit if results reach the lifecycle engines.
    }

    private fun mapToDomainReconciliationResult(
        identity: String,
        result: IntelligenceApplicationCommandResult
    ): IntelligenceReentryApplicationStateReconciliationResult {
        // Deterministic Adapter (Step 123 Requirement)
        val status = when (result) {
            is IntelligenceApplicationCommandResult.Success -> ApplicationStateReconciliationStatus.CONSISTENT
            else -> ApplicationStateReconciliationStatus.INCONSISTENT
        }

        return IntelligenceReentryApplicationStateReconciliationResult(
            reentryIdentity = identity,
            status = status,
            details = when (result) {
                is IntelligenceApplicationCommandResult.Failure -> "Processing failure: ${result.reason}"
                is IntelligenceApplicationCommandResult.Rejected -> "Domain rejection: ${result.reason}"
                is IntelligenceApplicationCommandResult.Cancelled -> "Explicit user cancellation."
                is IntelligenceApplicationCommandResult.TimedOut -> "Command exceeded authoritative deadline."
                else -> null
            },
            verifiedAt = result.processedAt
        )
    }
}
