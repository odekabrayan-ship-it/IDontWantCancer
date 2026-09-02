package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationHandoffTest {

    private val safetyGate = mockk<IntelligenceCommunicationSafetyGate>()
    private val integrityEngine = mockk<IntelligenceCommunicationPackageIntegrityEngine>()
    private val factory = IntelligenceCommunicationHandoffFactory(safetyGate, integrityEngine)

    @Test
    fun `valid READY and VALID package creates a success handoff`() {
        val pkg = createMockPackage()
        val safetyResult = CommunicationReadinessResult(CommunicationReadinessLevel.READY, "Safe", CommunicationSafetyReason.SATEISFIED, false, Instant.now())
        val integrityResult = IntelligenceCommunicationIntegrityResult("sig1", "sig1", CommunicationIntegrityStatus.VALID, emptyList(), Instant.now())

        every { safetyGate.evaluateSafety(pkg) } returns safetyResult
        every { integrityEngine.verifyPackageIntegrity(pkg) } returns integrityResult

        val result = factory.createHandoff(pkg)

        assertTrue(result is HandoffResult.Success)
        val success = result as HandoffResult.Success
        assertEquals("sig1", success.handoff.intelligenceId)
        assertEquals(CommunicationReadinessLevel.READY, success.handoff.communicationPackage.communicationReadiness.level)
    }

    @Test
    fun `BLOCKED safety gate results in handoff failure`() {
        val pkg = createMockPackage()
        val safetyResult = CommunicationReadinessResult(CommunicationReadinessLevel.BLOCKED, "Blocked", CommunicationSafetyReason.MISSING_PROVENANCE, true, Instant.now())

        every { safetyGate.evaluateSafety(pkg) } returns safetyResult

        val result = factory.createHandoff(pkg)

        assertTrue(result is HandoffResult.Failure)
        val failure = result as HandoffResult.Failure
        assertEquals(CommunicationReadinessLevel.BLOCKED, failure.readinessLevel)
    }

    @Test
    fun `INVALID integrity results in handoff failure`() {
        val pkg = createMockPackage()
        val safetyResult = CommunicationReadinessResult(CommunicationReadinessLevel.READY, "Safe", CommunicationSafetyReason.SATEISFIED, false, Instant.now())
        val integrityResult = IntelligenceCommunicationIntegrityResult("sig1", "sig1", CommunicationIntegrityStatus.INVALID, listOf(mockk(relaxed = true)), Instant.now())

        every { safetyGate.evaluateSafety(pkg) } returns safetyResult
        every { integrityEngine.verifyPackageIntegrity(pkg) } returns integrityResult

        val result = factory.createHandoff(pkg)

        assertTrue(result is HandoffResult.Failure)
        val failure = result as HandoffResult.Failure
        assertEquals(CommunicationIntegrityStatus.INVALID, failure.integrityStatus)
    }

    private fun createMockPackage() = IntelligenceCommunicationPackage(
        intelligenceId = "sig1",
        threadId = "t1",
        changeId = null,
        currentState = mockk(relaxed = true),
        continuity = null,
        narrative = mockk(relaxed = true),
        significanceLevel = SignificanceOutcome.SIGNIFICANT,
        priority = AttentionLevel.IMPORTANT,
        evidenceSynthesis = mockk(relaxed = true),
        confidence = SignalConfidence.HIGH,
        conflicts = emptyList(),
        evidenceGap = mockk(relaxed = true),
        relevance = null,
        freshness = null,
        communicationReadiness = CommunicationReadinessResult(CommunicationReadinessLevel.READY, "R", CommunicationSafetyReason.SATEISFIED, false, Instant.now()),
        provenance = mockk(relaxed = true),
        assembledAt = Instant.now()
    )
}
