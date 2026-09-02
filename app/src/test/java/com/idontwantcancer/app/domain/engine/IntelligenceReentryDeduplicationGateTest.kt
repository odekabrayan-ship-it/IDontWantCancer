package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryDeduplicationGateTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val gate = DefaultIntelligenceReentryDeduplicationGate(memory)

    @Test
    fun `first valid re-entry is NEW_REENTRY`() = runTest {
        val reentryResult = createReentryResult("sig1")
        val diff = createMockDiff("sig1", "state-new")
        
        // Memory has no record of this admission
        coEvery { memory.getSignalById("sig1") } returns null

        val result = gate.checkDeduplication(reentryResult, diff)

        assertEquals(DeduplicationStatus.NEW_REENTRY, result.status)
        assertEquals("sig1::state-new", result.reentryIdentity)
    }

    @Test
    fun `identical re-entry evaluated again is ALREADY_ADMITTED`() = runTest {
        val reentryResult = createReentryResult("sig1")
        val diff = createMockDiff("sig1", "state-already-seen")
        
        val signal = createMockSignal("sig1", "state-already-seen")
        coEvery { memory.getSignalById("sig1") } returns signal

        val result = gate.checkDeduplication(reentryResult, diff)

        assertEquals(DeduplicationStatus.ALREADY_ADMITTED, result.status)
    }

    @Test
    fun `same Signal with a different structured change is NEW_REENTRY`() = runTest {
        val reentryResult = createReentryResult("sig1")
        val diff = createMockDiff("sig1", "state-v3")
        
        // Memory has a different admission ID
        val signal = createMockSignal("sig1", "state-v2")
        coEvery { memory.getSignalById("sig1") } returns signal

        val result = gate.checkDeduplication(reentryResult, diff)

        assertEquals(DeduplicationStatus.NEW_REENTRY, result.status)
    }

    private fun createReentryResult(id: String) = IntelligenceReentryResult(
        intelligenceId = id,
        status = ReentryStatus.REENTRY_REQUIRED,
        triggers = listOf(ReentryTrigger.STATE_CHANGE),
        reason = "R",
        evaluatedAt = Instant.now()
    )

    private fun createMockDiff(id: String, currentStateId: String) = BriefingItemReconciliationDiff(
        intelligenceId = id,
        isSuperseded = false,
        stateChanged = true,
        significanceChanged = false,
        priorityChanged = false,
        evidenceChanged = false,
        conflictChanged = false,
        previousStateEntryId = "old",
        currentStateEntryId = currentStateId,
        previousSignificance = SignificanceOutcome.SIGNIFICANT,
        currentSignificance = SignificanceOutcome.SIGNIFICANT,
        previousPriority = AttentionLevel.ROUTINE,
        currentPriority = AttentionLevel.ROUTINE,
        previousEvidenceLevel = EvidenceSynthesisLevel.SUPPORTED,
        currentEvidenceLevel = EvidenceSynthesisLevel.SUPPORTED
    )

    private fun createMockSignal(id: String, lastAdmittedId: String) = Signal(
        id = id,
        title = "Title",
        summary = "Summ",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = SignalConfidence.HIGH,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source"),
        lastAdmittedStateEntryId = lastAdmittedId
    )
}
