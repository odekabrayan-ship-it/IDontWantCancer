package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceBriefingContinuityEngineTest {

    private val engine = DefaultIntelligenceBriefingContinuityEngine()

    @Test
    fun `new briefing item results in NEW continuity state`() {
        val itemChange = createMockItemChange(BriefingItemChangeLevel.NEW)
        val pkg = createMockPackage()

        val result = engine.determineContinuity("curr", "prev", itemChange, pkg)

        assertEquals(BriefingContinuityState.NEW, result.continuityState)
    }

    @Test
    fun `unchanged item results in CONTINUING state`() {
        val itemChange = createMockItemChange(BriefingItemChangeLevel.UNCHANGED)
        val pkg = createMockPackage()

        val result = engine.determineContinuity("curr", "prev", itemChange, pkg)

        assertEquals(BriefingContinuityState.CONTINUING, result.continuityState)
    }

    @Test
    fun `material change in continuity result maps to MATERIAL_CHANGE state`() {
        val itemChange = createMockItemChange(BriefingItemChangeLevel.MATERIALLY_CHANGED)
        val pkg = createMockPackage(continuityLevel = ContinuityLevel.MATERIAL_CHANGE)

        val result = engine.determineContinuity("curr", "prev", itemChange, pkg)

        assertEquals(BriefingContinuityState.MATERIAL_CHANGE, result.continuityState)
    }

    @Test
    fun `reversal in continuity result maps to REVERSAL state`() {
        val itemChange = createMockItemChange(BriefingItemChangeLevel.MATERIALLY_CHANGED)
        val pkg = createMockPackage(continuityLevel = ContinuityLevel.REVERSAL)

        val result = engine.determineContinuity("curr", "prev", itemChange, pkg)

        assertEquals(BriefingContinuityState.REVERSAL, result.continuityState)
    }

    private fun createMockItemChange(level: BriefingItemChangeLevel) = IntelligenceBriefingItemChange(
        signalId = "sig1",
        level = level,
        previousPosition = 0,
        currentPosition = 0,
        reason = "Reason"
    )

    private fun createMockPackage(
        continuityLevel: ContinuityLevel? = null
    ) = IntelligenceCommunicationPackage(
        intelligenceId = "sig1",
        threadId = "t1",
        changeId = null,
        currentState = mockk(relaxed = true),
        continuity = continuityLevel?.let { createMockContinuityResult(it) },
        narrative = mockk(relaxed = true),
        significanceLevel = SignificanceOutcome.SIGNIFICANT,
        priority = AttentionLevel.IMMEDIATE,
        evidenceSynthesis = mockk(relaxed = true),
        confidence = SignalConfidence.HIGH,
        conflicts = emptyList(),
        evidenceGap = mockk(relaxed = true),
        relevance = null,
        freshness = null,
        communicationReadiness = mockk(relaxed = true),
        provenance = mockk(relaxed = true),
        assembledAt = Instant.now()
    )

    private fun createMockContinuityResult(level: ContinuityLevel) = IntelligenceContinuityResult(
        level = level,
        threadId = "t1",
        signalId = "sig1",
        previousStateEntryId = "old",
        reason = "Reason",
        evaluatedAt = Instant.now()
    )
}
