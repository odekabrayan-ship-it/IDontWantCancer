package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityRequest
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of the terminal-state integrity boundary.
 * Prevents mutation or reopening of already-finalized commands.
 */
@Singleton
class DefaultIntelligenceCommandTerminalStateIntegrityBoundary @Inject constructor() : 
    IntelligenceCommandTerminalStateIntegrityBoundary {

    private val terminalLedger = ConcurrentHashMap<String, IntelligenceApplicationCommandResult>()
    private val finalityLedger = ConcurrentHashMap<String, IntelligenceCommandConsumptionFinalityResult>()

    override fun verifyIntegrity(operationId: String): Boolean {
        // Once a result is in the ledger, it is considered terminal 
        // for the purpose of preventing accidental state mutations.
        return !terminalLedger.containsKey(operationId)
    }

    override fun protectTerminality(
        identity: String,
        result: IntelligenceApplicationCommandResult
    ): IntelligenceApplicationCommandResult {
        // Enforce immutability of the terminal state.
        val existing = terminalLedger.putIfAbsent(identity, result)
        return existing ?: result
    }

    override fun protectFinality(
        request: IntelligenceCommandTerminalIntegrityRequest
    ): IntelligenceCommandConsumptionFinalityResult {
        // Enforce immutability of the terminal finality state.
        val identity = request.finalityResult.operationId
        val existing = finalityLedger.putIfAbsent(identity, request.finalityResult)
        return existing ?: request.finalityResult
    }
}
