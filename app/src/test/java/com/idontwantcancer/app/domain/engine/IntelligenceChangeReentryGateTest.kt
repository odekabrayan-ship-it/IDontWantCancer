package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceChangeReentryGateTest {

    private val gate = DefaultIntelligenceChangeReentryGate()

    @Test
    fun `unchanged historical and current intelligence does not re-enter`() {
        val diff = createMockDiff(id = "sig1")
        val signal = createMockSignal("sig1", SignificanceOutcome.SIGNIFICANT)

        val result = gate.evaluateReentry(diff, signal)

        assertEquals(ReentryStatus.NO_REENTRY, result.status)
        assertTrue(result.triggers.isEmpty())
    }

    @Test
    fun `materially changed state triggers REENTRY_REQUIRED`() {
        val diff = createMockDiff(id = "sig1", stateChanged = true)
        val signal = createMockSignal("sig1", SignificanceOutcome.SIGNIFICANT)

        val result = gate.evaluateReentry(diff, signal)

        assertEquals(ReentryStatus.REENTRY_REQUIRED, result.status)
        assertTrue(result.triggers.contains(ReentryTrigger.STATE_CHANGE))
    }

    @Test
    fun `supersession triggers REENTRY_REQUIRED`() {
        val diff = createMockDiff(id = "sig1", isSuperseded = true)
        val signal = createMockSignal("sig1", SignificanceOutcome.SIGNIFICANT)

        val result = gate.evaluateReentry(diff, signal)

        assertEquals(ReentryStatus.REENTRY_REQUIRED, result.status)
        assertTrue(result.triggers.contains(ReentryTrigger.SUPERSESSION_CHANGE))
    }

    @Test
    fun `evidence change triggers RE_EVALUATION_REQUIRED`() {
        val diff = createMockDiff(id = "sig1", evidenceChanged = true)
        val signal = createMockSignal("sig1", SignificanceOutcome.SIGNIFICANT)

        val result = gate.evaluateReentry(diff, signal)

        assertEquals(ReentryStatus.RE_EVALUATION_REQUIRED, result.status)
        assertTrue(result.triggers.contains(ReentryTrigger.SIGNIFICANT_EVIDENCE_CHANGE))
    }

    @Test
    fun `insignificant intelligence cannot re-enter for communication`() {
        val diff = createMockDiff(id = "sig1", stateChanged = true)
        val signal = createMockSignal("sig1", SignificanceOutcome.NOT_SIGNIFICANT)

        val result = gate.evaluateReentry(diff, signal)

        assertEquals(ReentryStatus.NO_REENTRY, result.status)
        assertTrue(result.reason.contains("not significant enough"))
    }

    private fun createMockDiff(
        id: String,
        isSuperseded: Boolean = false,
        stateChanged: Boolean = false,
        evidenceChanged: Boolean = false
    ) = BriefingItemReconciliationDiff(
        intelligenceId = id,
        isSuperseded = isSuperseded,
        stateChanged = stateChanged,
        significanceChanged = false,
        priorityChanged = false,
        evidenceChanged = evidenceChanged,
        conflictChanged = false,
        previousStateEntryId = "e1",
        currentStateEntryId = if (stateChanged) "e2" else "e1",
        previousSignificance = SignificanceOutcome.SIGNIFICANT,
        currentSignificance = SignificanceOutcome.SIGNIFICANT,
        previousPriority = AttentionLevel.ROUTINE,
        currentPriority = AttentionLevel.ROUTINE,
        previousEvidenceLevel = EvidenceSynthesisLevel.SUPPORTED,
        currentEvidenceLevel = EvidenceSynthesisLevel.SUPPORTED
    )

    private fun createMockSignal(id: String, significance: SignificanceOutcome) = Signal(
        id = id,
        title = "Title",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = SignalConfidence.HIGH,
        significanceLevel = significance,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source")
    )
}
