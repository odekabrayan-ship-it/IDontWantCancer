package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceStateReconciliationEngineTest {

    private val engine = DefaultIntelligenceStateReconciliationEngine()

    @Test
    fun `when no change detected in continuity, classification is NO_CHANGE`() {
        val thread = createThread("t1")
        val previous = createMockState("old")
        val current = createMockState("old") // Same entry
        val continuity = createMockContinuity(ContinuityLevel.CONTINUATION)
        val transition = null

        val result = engine.reconcileState(thread, previous, current, continuity, transition)

        assertEquals(ReconciliationClassification.NO_CHANGE, result.classification)
    }

    @Test
    fun `when material change detected in continuity, classification is MATERIALLY_CHANGED`() {
        val thread = createThread("t1")
        val previous = createMockState("old")
        val current = createMockState("new")
        val continuity = createMockContinuity(ContinuityLevel.MATERIAL_CHANGE)
        val transition = createMockTransition("new")

        val result = engine.reconcileState(thread, previous, current, continuity, transition)

        assertEquals(ReconciliationClassification.MATERIALLY_CHANGED, result.classification)
    }

    @Test
    fun `when unresolved conflict exists in state, classification is REQUIRES_REVIEW`() {
        val thread = createThread("t1")
        val current = createMockState("new", conflict = ResolutionStatus.UNRESOLVED)
        val continuity = createMockContinuity(ContinuityLevel.UPDATE)

        val result = engine.reconcileState(thread, null, current, continuity, null)

        assertEquals(ReconciliationClassification.REQUIRES_REVIEW, result.classification)
    }

    private fun createThread(id: String) = IntelligenceThread(
        id = id,
        topicIdentifier = "Topic",
        firstDetectedAt = Instant.now(),
        lastUpdatedAt = Instant.now()
    )

    private fun createMockState(entryId: String, conflict: ResolutionStatus = ResolutionStatus.RESOLVED) = ReconstructedState(
        threadId = "t1",
        summary = "Summary",
        effectiveEntryId = entryId,
        confidence = SignalConfidence.HIGH,
        conflictStatus = conflict,
        reconstructedAt = Instant.now()
    )

    private fun createMockContinuity(level: ContinuityLevel) = IntelligenceContinuityResult(
        level = level,
        threadId = "t1",
        signalId = "sig1",
        previousStateEntryId = "old",
        reason = "Reason",
        evaluatedAt = Instant.now()
    )

    private fun createMockTransition(entryId: String) = IntelligenceStateTransition(
        id = "tr1",
        threadId = "t1",
        previousStateEntryId = "old",
        resultingStateEntryId = entryId,
        type = IntelligenceStateTransitionType.UPDATED,
        triggeringEventId = "evt1",
        effectiveAt = Instant.now(),
        reason = "Reason"
    )
}
